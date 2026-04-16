package com.libreguardia.routing.modules

import com.libreguardia.config.UserSession
import com.libreguardia.db.Role
import com.libreguardia.dto.RefreshTokenDTO
import com.libreguardia.exception.InvalidCredentialsException
import com.libreguardia.service.AuthService
import com.libreguardia.util.userPrincipal
import com.libreguardia.util.userUuidFromJwt
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

@Resource("/api/auth")
class AuthAPI {
    @Resource("login")
    class Login (val parent: AuthAPI = AuthAPI())
    @Resource("logout")
    class Logout (val parent: AuthAPI = AuthAPI())
    @Resource("logout-all-devices")
    class LogoutAllDevices (val parent: AuthAPI = AuthAPI())
    @Resource("refresh-token")
    class RefreshToken (val parent: AuthAPI = AuthAPI())
}

fun Route.authRouting(
    authService: AuthService
) {
    authenticate("auth-form") {
        post<AuthAPI.Login> {
            val userEmail: String =
                call.principal<UserIdPrincipal>()?.name ?: throw InvalidCredentialsException()
            val userSession = authService.saveSession(userEmail = userEmail)
            call.sessions.set(userSession)
            call.respondRedirect("/")
        }
    }
}
/*
    authenticate {
        authorized(Role.USER, Role.ADMIN) {
            post<AuthAPI.Logout> {
                val refreshToken = call.receive<RefreshTokenDTO>()
                val userPrincipal = call.userPrincipal()
                authService.logout(
                    refreshTokenDTO = refreshToken,
                    userUuid = userPrincipal.uuid
                )
                call.respond(HttpStatusCode.NoContent)
            }
            post<AuthAPI.LogoutAllDevices> {
                val userUuid = call.userUuidFromJwt()
                authService.logoutAllDevices(
                    userUuid = userUuid
                )
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

 */