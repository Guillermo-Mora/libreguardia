package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.COOKIE_DURATION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.UserSession
import com.libreguardia.db.Role
import com.libreguardia.exception.InvalidCredentialsException
import com.libreguardia.repository.SessionRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.util.withTransaction
import io.ktor.server.auth.UserPasswordCredential
import java.util.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private val COOKIE_DURATION = 30.days
class AuthService(
    private val bcryptVerifyer: BCrypt.Verifyer,
    private val bcryptHasher: BCrypt.Hasher,
    private val clock: Clock.System,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend fun validateLogin(
        credentials: UserPasswordCredential
    ): Boolean {
        val fakeHashedPassword = $$"$2a$10$ppBBdNODqqvrTLd4wVAQ0OJ7i0hsmuJYcy69g/pPlfzmBD/pEPTMK"
        val userEntity = withTransaction { userRepository.getEntity(credentials.name) }
        val verificationResult = bcryptVerifyer.verify(
            credentials.password.toByteArray(),
            (userEntity?.password ?: fakeHashedPassword).toByteArray()
        )
        return !(!verificationResult.verified || userEntity?.isEnabled != true || userEntity.isDeleted)
    }

    suspend fun validateSession(
        userSession: UserSession
    ): UserPrincipal? {
        //Esta función debería de devolver de alguna forma el rol y el uuid del usuario,
        // para que el plugin custom de roles, y las páginas puedan usarlo para mostrar el contenido adecuado
        return withTransaction {
            val sessionEntity = sessionRepository.getSessionEntityWithUserLoaded(uuid = userSession.uuid)
                ?: return@withTransaction null
            if (
                sessionEntity.expiresAt <= clock.now() ||
                !sessionEntity.user.isEnabled ||
                sessionEntity.user.isDeleted
            ) return@withTransaction null
            UserPrincipal(
                userUuid = sessionEntity.user.id.value,
                userRole = sessionEntity.user.role
            )
        }
    }

    suspend fun saveSession(
        userEmail: String
    ): UserSession {
        return withTransaction {
            val userUuid = userRepository.getUserUuid(email = userEmail) ?: throw InvalidCredentialsException()
            val sessionUuid = UUID.randomUUID()
            sessionRepository.save(
                uuid = sessionUuid,
                userUuid = userUuid,
                expiration = clock.now().plus(COOKIE_DURATION.toDuration(DurationUnit.SECONDS)),
            )
            UserSession(
                uuid = sessionUuid
            )
        }
    }
}

    //My first authentication implementation, using JWT (not usable with HTMX, which should be managed with cookies)
    // I didn't investigate how HTMX authentication was commonly implemented, so I implemented my own JWT
    // auth thinking I could easily use it with HTMX frontend
    /*
    suspend fun validateLogin(
        credentials: LoginDTO
    ): LoginResponseDTO {
        val fakeHashedPassword = $$"$2a$10$ppBBdNODqqvrTLd4wVAQ0OJ7i0hsmuJYcy69g/pPlfzmBD/pEPTMK"
        val userEntity = withTransaction { userRepository.getEntity(credentials.email) }
        val verificationResult = bcryptVerifyer.verify(
            credentials.password.toByteArray(),
            (userEntity?.password ?: fakeHashedPassword).toByteArray()
        )
        if (!verificationResult.verified || userEntity?.isEnabled != true || userEntity.isDeleted)
            throw InvalidCredentialsException()
        val accessToken = jwtService.createAccessToken(
            uuid = userEntity.id.value,
            role = userEntity.role
        )
        val refreshToken = createRefreshToken()
        val refreshTokenPrefix = refreshToken.substring(0, REFRESH_TOKEN_PREFIX_LENGTH)
        val refreshTokenHash = bcryptHasher.hashToString(
            BCRYPT_HASH_COST,
            refreshToken.substring(REFRESH_TOKEN_PREFIX_LENGTH).toCharArray()
        )
        withTransaction {
            refreshTokenRepository.save(
                newRefreshTokenPrefix = refreshTokenPrefix,
                newRefreshTokenHash = refreshTokenHash,
                expiration = clock.now().plus(REFRESH_TOKEN_DURATION),
                userUuid = userEntity.id.value
            )
        }
        return LoginResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    suspend fun refreshToken(
        refreshTokenDTO: RefreshTokenDTO
    ): LoginResponseDTO {
        val refreshTokenPrefix = refreshTokenDTO.refreshToken.substring(0, REFRESH_TOKEN_PREFIX_LENGTH)
        val refreshTokenEntity = withTransaction { refreshTokenRepository.getRefreshTokenEntity(refreshTokenPrefix) }
            ?: throw InvalidRefreshTokenException()
        val refreshTokenHash = refreshTokenDTO.refreshToken.substring(REFRESH_TOKEN_PREFIX_LENGTH)
        if (refreshTokenEntity.expiresAt <= clock.now()) {
            withTransaction { refreshTokenRepository.deleteRefreshToken(refreshTokenEntity.id.value) }
            throw InvalidRefreshTokenException()
        }
        validateRefreshToken(
            refreshToken = refreshTokenHash,
            hashedRefreshToken = refreshTokenEntity.refreshTokenHash
        )
        val newRefreshToken = createRefreshToken()
        val newRefreshTokenPrefix = newRefreshToken.substring(0, REFRESH_TOKEN_PREFIX_LENGTH)
        val newRefreshTokenHash = bcryptHasher.hashToString(
            BCRYPT_HASH_COST,
            newRefreshToken.substring(REFRESH_TOKEN_PREFIX_LENGTH).toCharArray()
        )
        withTransaction {
            refreshTokenRepository.save(
                newRefreshTokenPrefix = newRefreshTokenPrefix,
                newRefreshTokenHash = newRefreshTokenHash,
                expiration = clock.now().plus(REFRESH_TOKEN_DURATION),
                userUuid = refreshTokenEntity.user.id.value
            )
            if (!refreshTokenRepository.deleteRefreshToken(refreshTokenEntity.id.value))
                throw InvalidRefreshTokenException()
        }
        val accessToken = jwtService.createAccessToken(
            uuid = refreshTokenEntity.user.id.value,
            role = refreshTokenEntity.user.role
        )
        return LoginResponseDTO(
            accessToken = accessToken,
            refreshToken = newRefreshToken
        )
    }

    suspend fun logout(
        refreshTokenDTO: RefreshTokenDTO,
        userUuid: UUID
    ) {
        val refreshTokenPrefix = refreshTokenDTO.refreshToken.substring(0, REFRESH_TOKEN_PREFIX_LENGTH)
        val refreshTokenHash = refreshTokenDTO.refreshToken.substring(REFRESH_TOKEN_PREFIX_LENGTH)
        val hashedRefreshToken = withTransaction {
            refreshTokenRepository.getRefreshTokenHash(
                refreshTokenPrefix = refreshTokenPrefix,
                userUuid = userUuid
            )
        } ?: throw InvalidRefreshTokenException()
        validateRefreshToken(
            refreshToken = refreshTokenHash,
            hashedRefreshToken = hashedRefreshToken
        )
        withTransaction {
            if (!refreshTokenRepository.deleteRefreshToken(
                    refreshTokenPrefix = refreshTokenPrefix,
                    userUUID = userUuid
                )
            ) throw InvalidRefreshTokenException()
        }
    }

    suspend fun logoutAllDevices(
        userUuid: UUID
    ) {
        withTransaction {
            refreshTokenRepository.deleteRefreshTokensByUser(userUUID = userUuid)
        }
    }

    private fun createRefreshToken(): String = UUID.randomUUID().toString().replace("-", "")
    private fun validateRefreshToken(
        refreshToken: String,
        hashedRefreshToken: String
    ) {
        val verificationResult = bcryptVerifyer.verify(
            refreshToken.toByteArray(),
            hashedRefreshToken.toByteArray()
        )
        if (!verificationResult.verified) throw InvalidRefreshTokenException()
    }
     */