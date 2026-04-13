package com.libreguardia.exception

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = cause.reasons.joinToString()
            )
        }
        exception<RoleNotFoundException> { call, _ ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = ErrorCode.ROLE_NOT_FOUND
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
        exception<ExposedSQLException> { call, cause ->
            val sqlState = (cause.cause as PSQLException).sqlState
            when (sqlState) {
                "23505" -> call.respond(
                    status = HttpStatusCode.Conflict,
                    message = ErrorCode.DUPLICATED_UNIQUE_FIELD
                )

                else -> call.respond(
                    //TEMPORARY FOR DEBUGGING
                    status = HttpStatusCode.InternalServerError,
                    //message = ErrorCode.UNKNOWN_SERVER_ERROR
                    message = cause.cause.toString()
                )
            }
        }
        //En vez de responder así, se podría responder enviando una página htmx, cómo la del login.
        // Y así redirigirlo ahí.
        exception<InvalidRefreshTokenException> { call, _ ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = ErrorCode.INVALID_REFRESH_TOKEN
            )
        }
        exception<InsufficientPermissionsException> { call, _ ->
            call.respond(
                status = HttpStatusCode.Forbidden,
                message = ErrorCode.INSUFFICIENT_PERMISSIONS
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