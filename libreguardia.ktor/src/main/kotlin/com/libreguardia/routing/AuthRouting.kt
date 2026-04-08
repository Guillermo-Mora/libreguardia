package com.libreguardia.routing

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
    class Login
}

fun Route.authRouting(
    authService: AuthService
) {
    post<AuthAPI.Login> {
        val credentials = call.receive<LoginDTO>()
        val jwt = authService.validateLogin(
            credentials = credentials
        )
        call.respond(jwt)
    }
}