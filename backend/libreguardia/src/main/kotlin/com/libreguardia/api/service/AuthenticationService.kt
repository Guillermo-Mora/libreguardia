package com.libreguardia.api.service

import com.libreguardia.api.dto.LoginResponseDto
import com.libreguardia.api.dto.LogintRequestDto
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService (
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {
    fun login(
        loginRequestDto: LogintRequestDto
    ): LoginResponseDto {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequestDto.email,
                loginRequestDto.password
            )
        )
        return if (authentication.isAuthenticated)
            loginResponse(jwtService.generateToken(loginRequestDto.email))
        else loginResponse(null)
    }

    private fun loginResponse(token: String?) = LoginResponseDto(
        message = if (token == null) "Invalid credentials" else "Login succeded",
        token = token
    )
}