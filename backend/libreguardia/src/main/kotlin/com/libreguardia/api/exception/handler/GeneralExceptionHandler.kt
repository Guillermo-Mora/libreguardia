package com.libreguardia.api.exception.handler

import com.libreguardia.api.exception.UserRoleNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GeneralExceptionHandler : BaseExceptionHandler() {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMessageNotReadable() = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        createResponseEntity("Invalid data format")
    )

    @ExceptionHandler(UserRoleNotFoundException::class)
    fun handleUserRoleNotFound(e: UserRoleNotFoundException) = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        createResponseEntity(e.message)
    )
}