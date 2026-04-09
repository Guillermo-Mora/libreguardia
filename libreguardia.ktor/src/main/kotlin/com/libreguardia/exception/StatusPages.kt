package com.libreguardia.exception

import com.libreguardia.exception.ErrorCode
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
        exception<UserRoleNotFoundException> { call, _ ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = ErrorCode.USER_ROLE_NOT_FOUND
            )
        }
        exception<UserNotFoundException> { call, _ ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = ErrorCode.USER_NOT_FOUND
            )
        }
        exception<UserAlreadyDeletedException> { call, _ ->
            call.respond(
                status = HttpStatusCode.Conflict,
                message = ErrorCode.USER_ALREADY_DELETED
            )
        }
        exception<IncorrectPasswordException> { call, _ ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ErrorCode.INCORRECT_PASSWORD
            )
        }
        exception<InvalidCredentialsException> { call, _ ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = ErrorCode.INVALID_CREDENTIALS
            )
        }
        exception<InvalidRefreshTokenException> { call, _ ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = ErrorCode.INVALID_REFRESH_TOKEN
            )
        }
        //TEMPORARY FOR DEBUGGING
        exception<BadRequestException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                //message = "ErrorCode.BAD_REQUEST"
                message = cause.toString()
            )
        }
        //TEMPORARY FOR DEBUGGING
        exception<Throwable> { call, cause ->
            call.respondText(
                status = HttpStatusCode.InternalServerError,
                //text = "ErrorCode.UNKNOWN_SERVER_ERROR"
                text = cause.toString()
            )
        }
    }
}