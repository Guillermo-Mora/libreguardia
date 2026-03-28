package com.libreguardia.routing

import com.libreguardia.service.UserService
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.userRoute(
    userService: UserService
) {
    route("/users") {
        get {

        }
        post {

        }
        delete {

        }
        patch("/byEmail/{userEmail}") {

        }
    }
}