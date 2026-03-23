package com.libreguardia.api.exception.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthenticationExceptionHandler : BaseExceptionHandler() {
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
}