package com.libreguardia.api.controller

import com.libreguardia.api.dto.LoginResponseDto
import com.libreguardia.api.dto.LogintRequestDto
import com.libreguardia.api.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/authentication")
class AuthenticationController (
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/login")
    fun loginAndGetToken(@RequestBody loginRequestDto: LogintRequestDto): LoginResponseDto {
        return authenticationService.login(loginRequestDto)
    }
}