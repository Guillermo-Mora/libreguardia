package com.libreguardia.api.controller

import com.libreguardia.api.dto.LoginRequestDto
import com.libreguardia.api.service.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/authentication")
class AuthenticationController (
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    @PostMapping("/login")
    fun loginAndGetToken(@RequestBody loginRequestDto: LoginRequestDto): String {
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequestDto.email,
                    loginRequestDto.password
                )
            )
            println("Authentication result: ${authentication.isAuthenticated}")
            return if (authentication.isAuthenticated) jwtService.generateToken(loginRequestDto.email)
            else throw UsernameNotFoundException("Invalid user request!")
        } catch (e: Exception) {
            println("Authentication failed: ${e.javaClass.name} - ${e.message}")
            throw e
        }
    }
}