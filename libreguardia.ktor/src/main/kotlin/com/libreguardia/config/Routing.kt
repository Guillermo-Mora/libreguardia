package com.libreguardia.config


import com.libreguardia.routing.courseRouting
import com.libreguardia.routing.userRouting
import com.libreguardia.service.CourseService
import com.libreguardia.service.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService? = null,
    courseService: CourseService? = null
) {
    //Unified routes for pages and for obtaining content (get, post, put, patch)
    install(Resources)
    routing {
        userService?.let {
            userRouting(
                userService = it
            )
        }
        courseService?.let {
            courseRouting(
                service = it
            )
        }


        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}