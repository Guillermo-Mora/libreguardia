package com.libreguardia.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GeneralExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMessageNotReadable() = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        mapOf(
            "message" to "Invalid data format"
        )
    )
}