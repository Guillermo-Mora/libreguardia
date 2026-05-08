package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.COOKIE_DURATION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.UserSession
import com.libreguardia.exception.InvalidCredentialsException
import com.libreguardia.repository.SessionRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.util.withTransaction
import io.ktor.server.auth.UserPasswordCredential
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import java.util.*
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AuthService(
    private val bcryptVerifyer: BCrypt.Verifyer,
    private val clock: Clock.System,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend fun validateLogin(
        credentials: UserPasswordCredential
    ): Boolean {
        //
        println(credentials)
        //
        val fakeHashedPassword = $$"$2a$10$ppBBdNODqqvrTLd4wVAQ0OJ7i0hsmuJYcy69g/pPlfzmBD/pEPTMK"
        val userEntity = withTransaction { userRepository.getEntity(credentials.name) }
        println(userEntity?.password)
        val verificationResult = bcryptVerifyer.verify(
            credentials.password.toByteArray(),
            (userEntity?.password ?: fakeHashedPassword).toByteArray()
        )
        println(verificationResult.verified)
        return !(!verificationResult.verified || userEntity?.isEnabled != true || userEntity.isDeleted)
    }

    suspend fun validateSession(
        userSession: UserSession
    ): UserPrincipal? {
        val userSessionModel = withTransaction {
            //Replaced the previous operation using entities to now DSL. As this
            // validation is performed on every request to protected resources,
            // so it is key that is as efficient as possible.
            addLogger(StdOutSqlLogger)
            sessionRepository.getUserSessionModel(uuid = userSession.uuid)
        } ?: return null
        if (
            userSessionModel.expiresAt <= clock.now() ||
            !userSessionModel.isEnabled ||
            userSessionModel.isDeleted
        ) return null
        return UserPrincipal(
            userUuid = userSessionModel.userUuid,
            userRole = userSessionModel.userRole
        )
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

    suspend fun logout(
        sessionUuid: UUID
    ) {
        withTransaction {
            sessionRepository.deleteSession(uuid = sessionUuid)
        }
    }

    suspend fun logoutAllDevices(
        userUuid: UUID
    ) {
        withTransaction {
            sessionRepository.deleteSessionsFromUser(userUuid = userUuid)
        }
    }
}