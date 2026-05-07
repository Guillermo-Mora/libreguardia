package com.libreguardia.config

import com.libreguardia.db.Role
import com.libreguardia.service.AuthService
import com.libreguardia.util.UUIDSerializer
import io.ktor.htmx.html.hx
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.*
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.span
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
                call.respondHtmlFragment() {
                    @OptIn(ExperimentalKtorApi::class)
                    div("input-div input-div-error") {
                        this.id = "password-div"
                        label {
                            htmlFor = "password"
                            text("Password")
                        }
                        input {
                            attributes.hx {
                                validate = true
                            }
                            type = InputType.password
                            name = "password"
                            this.id = "password"
                            placeholder = "Input your password"
                            required = true
                        }
                        div("error-div") {
                            span {
                                text("Invalid credentials")
                            }
                        }
                    }
                }
            }
        }

        session<UserSession>(
            name = AUTH_SESSION
        ) {
            validate { session ->
                authService.validateSession(session)
            }
            challenge {
                if (call.request.headers["HX-Request"] == "true") {
                    call.response.headers.append("HX-Redirect", "/login")
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    call.respondRedirect("/login")
                }
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