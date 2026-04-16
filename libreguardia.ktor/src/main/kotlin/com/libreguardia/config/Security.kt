package com.libreguardia.config

import com.libreguardia.exception.ErrorCode
import com.libreguardia.service.AuthService
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import java.util.*

const val COOKIE_DURATION: Long = 2_592_000
const val BCRYPT_HASH_COST = 10
fun Application.configureSecurity(
    authService: AuthService
) {
    install(Sessions) {
        cookie<UserSession>(
            name = "user_session"
        ) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = COOKIE_DURATION
            cookie.httpOnly = true
            //secure = true,
            //Temporary for development
            cookie.secure = false
        }
    }

    install(Authentication) {
        form(
            name = "auth-form"
        ) {
            userParamName = "email"
            passwordParamName = "password"
            validate { credentials ->
                if (authService.validateLogin(credentials))
                    UserIdPrincipal(name = credentials.name)
                else null
            }
            challenge {
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = ErrorCode.INVALID_CREDENTIALS
                )
            }
        }

        session<UserSession>(
            name = "auth-session"
        ) {
            validate { session ->
                if (authService.validateSession(session))
                    session
                else null
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}

@Serializable
data class UserSession (
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userUuid: UUID
)

