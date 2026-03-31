package com.libreguardia.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = cause.reasons.joinToString()
            )
        }
        exception<UserRoleNotFoundException> { call, cause ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "Role name ${cause.roleName} not found"
            )
        }
        exception<UserNotFoundException> { call, cause ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "User with UUID ${cause.uuid} not found"
            )
        }
        exception<BadRequestException> {call, _ ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Bad Request"
            )
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
}