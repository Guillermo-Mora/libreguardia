package com.libreguardia.config


import com.libreguardia.routing.userRouting
import com.libreguardia.service.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService
) {
    //Unified routes for pages and for obtaining content (get, post, put, patch)
    install(Resources)
    routing {
        userRouting(
            userService = userService
        )


        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}