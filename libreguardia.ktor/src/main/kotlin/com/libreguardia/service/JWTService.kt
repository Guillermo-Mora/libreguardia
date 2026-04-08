package com.libreguardia.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.libreguardia.config.withTransaction
import com.libreguardia.db.UserEntity
import com.libreguardia.repository.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date
import java.util.UUID

class JwtService(
    private val application: Application,
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
        role: String
    ): String = createJwtToken(
        uuid = uuid,
        role = role,
        expireIn = 3_600_000
    )

    fun createRefreshToken(
        uuid: UUID,
        role: String
    ): String = createJwtToken(
        uuid = uuid,
        role = role,
        expireIn = 86_400_000
    )

    private fun createJwtToken(
        uuid: UUID,
        role: String,
        expireIn: Int
    ): String =
        JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("uuid", uuid.toString())
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + expireIn))
            .sign(
                //Algorithm.RSA256(
                //    publicKey =,
                //    privateKey =
                //)
                //Debe de obteners el secret como variable de entorno
                Algorithm.HMAC256(secret))

    suspend fun customValidator(
        credential: JWTCredential,
    ): JWTPrincipal? {
        val uuid: String? = extractUuid(credential)
        val foundUser: UserEntity? = uuid?.let(withTransaction { userRepository::getEntityWithRoleLoaded })

        return foundUser?.let {
            if (audienceMatches(credential))
                JWTPrincipal(credential.payload)
            else
                null
        }
    }

    private fun audienceMatches(
        credential: JWTCredential,
    ): Boolean =
        credential.payload.audience.contains(audience)

    fun audienceMatches(
        audience: String
    ): Boolean =
        this.audience == audience

    private fun getConfigProperty(
        path: String
    ) = application.environment.config.property(path).getString()

    private fun extractUuid(
        credential: JWTCredential
    ): String? = credential.payload.getClaim("uuid").asString()
}