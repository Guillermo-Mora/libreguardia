package com.libreguardia.routing.modules

import com.libreguardia.db.Role
import com.libreguardia.dto.LoginDTO
import com.libreguardia.exception.InvalidCredentialsException
import com.libreguardia.config.authorized
import com.libreguardia.dto.RefreshTokenDTO
import com.libreguardia.service.AuthService
import com.libreguardia.util.userUuidFromJwt
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import java.util.UUID

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
    post<AuthAPI.Login> {
        val credentials = call.receive<LoginDTO>()
        val tokens = authService.validateLogin(
            credentials = credentials
        )
        call.respond(tokens)
    }
    post<AuthAPI.RefreshToken> {
        val currentRefreshToken = call.receive<RefreshTokenDTO>()
        val tokens = authService.refreshToken(
            refreshTokenDTO = currentRefreshToken
        )
        call.respond(tokens)
    }
    authenticate {
        authorized(Role.USER, Role.ADMIN) {
            post<AuthAPI.Logout> {
                val refreshToken = call.receive<RefreshTokenDTO>()
                val userUuid = call.userUuidFromJwt()
                authService.logout(
                    refreshTokenDTO = refreshToken,
                    userUuid = userUuid
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
}