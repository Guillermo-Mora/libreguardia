package com.libreguardia.config

import com.libreguardia.db.Role
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
const val AUTH_FORM = "auth_form"
const val AUTH_SESSION = "auth_session"
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
            name = AUTH_FORM
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
            name = AUTH_SESSION
        ) {
            validate { session ->
                authService.validateSession(session)
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
    val uuid: UUID
)

data class UserPrincipal (
    val userUuid: UUID,
    val userRole: Role
)