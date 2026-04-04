package com.libreguardia.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.util.rootCause

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
                message = "Role with UUID ${cause.uuid} not found"
            )
        }
        exception<UserNotFoundException> { call, cause ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "User with UUID ${cause.uuid} not found"
            )
        }
        exception<UserAlreadyDeletedException> { call, cause ->
            call.respond(
                status = HttpStatusCode.Conflict,
                message = "User with UUID ${cause.uuid} already deleted"
            )
        }
        exception<IncorrectPasswordException> {call, _ ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Incorrect password"
            )
        }
        //TEMPORARY FOR DEBUGGING
        exception<BadRequestException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                //message = "Bad Request"
                message = cause.toString()
            )
        }
        //TEMPORARY FOR DEBUGGING
        exception<Throwable> { call, cause ->
            call.respondText(
                status = HttpStatusCode.InternalServerError,
                //text = "Unknown server error"
                text = cause.toString()
            )
        }
    }
}