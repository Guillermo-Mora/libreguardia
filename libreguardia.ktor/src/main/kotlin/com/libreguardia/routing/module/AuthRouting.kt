package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_FORM
import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.UserSession
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.exception.InvalidCredentialsException
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.service.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.sessions.*

@Resource("/auth")
class AuthAPI {
    @Resource("login")
    class Login(val parent: AuthAPI = AuthAPI())

    @Resource("logout")
    class Logout(val parent: AuthAPI = AuthAPI())

    @Resource("logout-all-devices")
    class LogoutAllDevices(val parent: AuthAPI = AuthAPI())
}

fun Route.authRouting(
    authService: AuthService
) {
    authenticate(AUTH_FORM) {
        post<AuthAPI.Login> {
            val userEmail: String =
                call.principal<UserIdPrincipal>()?.name ?: throw InvalidCredentialsException()
            val userSession = authService.saveSession(userEmail = userEmail)
            call.sessions.set(userSession)
            call.response.headers.append("HX-Redirect", "/")
            call.respond(HttpStatusCode.OK)
        }
    }
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN, Role.USER, Role.VISUALIZER) {
            post<AuthAPI.Logout> {
                //TEMPORARY ERROR THROWN, THIS SHOULD BE HANDLED IN A BETTER WAY
                val sessionUuid = call.sessions.get<UserSession>()?.uuid ?: throw UserNotFoundException()
                authService.logout(
                    sessionUuid = sessionUuid
                )
                call.response.headers.append("HX-Redirect", "/login")
            }
            post<AuthAPI.LogoutAllDevices> {
                //TEMPORARY ERROR THROWN, THIS SHOULD BE HANDLED IN A BETTER WAY
                println(call.principal<UserPrincipal>())
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw UserNotFoundException()
                authService.logoutAllDevices(
                    userUuid = userUuid
                )
                call.response.headers.append("HX-Redirect", "/login")
            }
        }
    }
}