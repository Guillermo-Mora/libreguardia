package com.libreguardia.api.controller

import com.libreguardia.api.dto.LoginResponseDto
import com.libreguardia.api.dto.LogintRequestDto
import com.libreguardia.api.service.AuthenticationService
import com.libreguardia.api.service.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/authentication")
class AuthenticationController (
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/login")
    fun loginAndGetToken(@RequestBody loginRequestDto: LogintRequestDto): LoginResponseDto {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequestDto.email,
                loginRequestDto.password
            )
        )
        return authenticationService.loginResponse(
            if (authentication.isAuthenticated) jwtService.generateToken(loginRequestDto.email) else null
        )
    }
}