package com.libreguardia.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthenticationExceptionHandler {
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials() = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        createResponseEntity("Invalid credentials")
    )

    @ExceptionHandler(DisabledException::class)
    fun handleDisabled() = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        createResponseEntity("Account disabled")
    )

    @ExceptionHandler(LockedException::class)
    fun handleLocked() = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        createResponseEntity("Account locked")
    )

    private fun createResponseEntity(message: String) = mapOf("message" to message)
}