package com.libreguardia.routing


import com.libreguardia.routing.modules.authRouting
import com.libreguardia.routing.modules.entryRouting
import com.libreguardia.routing.modules.userRouting
import com.libreguardia.service.AuthService
import com.libreguardia.service.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.resources.*
import io.ktor.server.routing.*
fun Application.configureRouting(
    authService: AuthService,
    userService: UserService
) {
    install(Resources)
    routing {
        entryRouting()
        authRouting(
            authService = authService
        )
        userRouting(
            userService = userService
        )
        staticResources("/static", "static")
    }
}