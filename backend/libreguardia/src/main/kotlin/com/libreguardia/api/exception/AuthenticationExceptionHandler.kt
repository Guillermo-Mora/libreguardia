package com.libreguardia.api.exception

import com.libreguardia.api.dto.LoginResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthenticationExceptionHandler {
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials() = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        LoginResponseDto(
            success = false,
            message = "Invalid credentials",
            token = null
        )
    )
}