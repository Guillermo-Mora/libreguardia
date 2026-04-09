package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.BCRYPT_HASH_COST
import com.libreguardia.dto.LoginDTO
import com.libreguardia.dto.LoginResponseDTO
import com.libreguardia.exception.IncorrectPasswordException
import com.libreguardia.exception.InvalidCredentialsException
import com.libreguardia.exception.InvalidRefreshTokenException
import com.libreguardia.repository.RefreshTokenRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.utils.withTransaction
import java.util.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

private const val REFRESH_TOKEN_PREFIX_LENGTH = 8
private val REFRESH_TOKEN_DURATION = 7.days
class AuthService(
    private val bcryptVerifyer: BCrypt.Verifyer,
    private val bcryptHasher: BCrypt.Hasher,
    private val clock: Clock.System,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    //Fake hashed password used to prevent timing attacks for discovering valid emails
    suspend fun validateLogin(
        credentials: LoginDTO
    ): LoginResponseDTO {
        val fakeHashedPassword = $$"$2a$10$ppBBdNODqqvrTLd4wVAQ0OJ7i0hsmuJYcy69g/pPlfzmBD/pEPTMK"
        val userEntity = withTransaction { userRepository.getEntityWithRoleLoaded(credentials.email) }
        val verificationResult = bcryptVerifyer.verify(
            credentials.password.toByteArray(),
            (userEntity?.password ?: fakeHashedPassword).toByteArray()
        )
        if (!verificationResult.verified || userEntity?.isEnabled != true) throw InvalidCredentialsException()
        val accessToken = jwtService.createAccessToken(
            uuid = userEntity.id.value,
            //Temporary for testing
            role = withTransaction { userEntity.userRole.name }
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

    suspend fun refreshToken(refreshToken: String): LoginResponseDTO {
        val refreshTokenPrefix = refreshToken.substring(0, REFRESH_TOKEN_PREFIX_LENGTH)
        val refreshTokenEntity = withTransaction { refreshTokenRepository.getRefreshTokenEntity(refreshTokenPrefix) }
            ?: throw InvalidRefreshTokenException()
        val refreshTokenHash = refreshToken.substring(REFRESH_TOKEN_PREFIX_LENGTH)
        if (refreshTokenEntity.expiresAt <= clock.now()) {
            withTransaction { refreshTokenRepository.deleteRefreshToken(refreshTokenEntity.id.value) }
            throw InvalidRefreshTokenException()
        }
        val verificationResult = bcryptVerifyer.verify(
            refreshTokenHash.toByteArray(),
            refreshTokenEntity.refreshTokenHash.toByteArray()
        )
        if (!verificationResult.verified) throw InvalidRefreshTokenException()
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
            refreshTokenRepository.deleteRefreshToken(refreshTokenEntity.id.value)
        }
        val accessToken = jwtService.createAccessToken(
            uuid = refreshTokenEntity.user.id.value,
            role = refreshTokenEntity.user.userRole.name
        )
        return LoginResponseDTO(
            accessToken = accessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun createRefreshToken(): String = UUID.randomUUID().toString().replace("-", "")
}