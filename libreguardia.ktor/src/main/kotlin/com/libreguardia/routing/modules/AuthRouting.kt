package com.libreguardia.routing.modules

import com.libreguardia.dto.LoginDTO
import com.libreguardia.service.AuthService
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@Resource("/api/auth")
class AuthAPI {
    @Resource("login")
    class Login (val parent: AuthAPI = AuthAPI())
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
        val currentRefreshToken = call.receive<String>()
        val tokens = authService.refreshToken(
            refreshToken = currentRefreshToken
        )
        call.respond(tokens)
    }
}