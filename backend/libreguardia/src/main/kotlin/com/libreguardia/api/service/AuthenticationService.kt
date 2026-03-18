package com.libreguardia.api.service

import com.libreguardia.api.dto.LoginResponseDto
import com.libreguardia.api.dto.LogintRequestDto
import com.libreguardia.api.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService (
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository
) {
    fun login(
        loginRequestDto: LogintRequestDto
    ): LoginResponseDto {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequestDto.email,
                loginRequestDto.password
            )
        )
        return LoginResponseDto(
            message = "Login succeded",
            token = jwtService.generateToken(
                email = loginRequestDto.email,
                role = userRepository.findRoleNameByEmail(loginRequestDto.email)
            )
        )
    }
}