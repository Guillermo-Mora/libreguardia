package com.libreguardia.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.libreguardia.db.Role
import com.libreguardia.db.model.UserEntity
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.repository.UserRepository
import com.libreguardia.util.withTransaction
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtService(
    //private val application: Application,
    private val userRepository: UserRepository
) {
    //private val secret = getConfigProperty("jwt.secret")
    //private val issuer = getConfigProperty("jwt.issuer")
    //private val audience = getConfigProperty("jwt.audience")
    //val realm = getConfigProperty("jwt.realm")
    //TEMPORARY FOR DEVELOPMENT AND TESTING
    //In the final application, it would be great if the user could set the names for these
    // in the application configuration file.
    private val secret = "top-secret"
    private val issuer = "libreguardia-api"
    private val audience = "libreguardia-client"
    val realm = "Access to Libreguardia API"

    val jwtVerifier: JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    fun createAccessToken(
        uuid: UUID,
        role: Role,
        //1 hour duration
        expireIn: Int = 3_600_000
    ): String =
        JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("uuid", uuid.toString())
            .withClaim("role", role.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + expireIn))
            .sign(Algorithm.HMAC256(secret))

    suspend fun customValidator(
        credential: JWTCredential,
    ): JWTPrincipal {
        val uuid = credential.payload.getClaim("uuid").asString()
        withTransaction {
            val user: UserEntity = userRepository.getEntity(UUID.fromString(uuid)) ?: throw UserNotFoundException()
            if (!user.isEnabled || user.isDeleted) throw UserNotFoundException()
        }
        return JWTPrincipal(credential.payload)
    }
}