package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.interfaces.DecodedJWT
import com.libreguardia.config.InvalidCredentialsException
import com.libreguardia.config.InvalidRefreshTokenException
import com.libreguardia.config.UserNotFoundException
import com.libreguardia.config.withTransaction
import com.libreguardia.dto.LoginDTO
import com.libreguardia.dto.LoginResponseDTO
import com.libreguardia.repository.UserRepository
import java.util.UUID

class AuthService(
    private val bcryptVerifyer: BCrypt.Verifyer,
    private val jwtService: JwtService,
    private val userRepository: UserRepository
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
            role = userEntity.userRole.name
        )
        val refreshToken = jwtService.createRefreshToken(
            uuid = userEntity.id.value,
            role = userEntity.userRole.name
        )
        //To implement hash the refresh token and save it in the refresh token repository
        // (I will need a new table in the DB)
        return LoginResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    suspend fun refreshToken(token: String): String? {
        val decodedRefreshToken = verifyRefreshToken(token)
        val userUuid = refreshTokenRepository.findUsernameByToken(token) ?: throw UserNotFoundException("")
        val user = withTransaction { userRepository.getEntityWithRoleLoaded(UUID.fromString(userUuid)) }
            ?: throw UserNotFoundException(userUuid)
        val userUuidFromRefreshToken: String = decodedRefreshToken.getClaim("uuid").asString()
        if (userUuid == userUuidFromRefreshToken)
            jwtService.createAccessToken(
                UUID.fromString(userUuid),
                role = TODO()
            )
    }

    private fun verifyRefreshToken(token: String): DecodedJWT {
        val decodedJwt: DecodedJWT = try {
            jwtService.jwtVerifier.verify(token)
        } catch (_: Exception) {
            throw InvalidRefreshTokenException()
        }
        return decodedJwt
    }
}