package com.libreguardia.api.service

import com.libreguardia.api.dto.LoginResponseDto
import com.libreguardia.api.repository.UserRepository
import com.libreguardia.api.security.UserAppDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthenticationService (
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findByEmail(email)
            ?.let {
                UserAppDetails(
                    userName = it.name,
                    userSurname = it.surname,
                    userEmail = it.email,
                    userPhoneNumber = it.phoneNumber,
                    userIsActive = it.isActive,
                    userPassword = it.password,
                    userRoleName = it.userRole.name,
                )
            }
            ?: throw UsernameNotFoundException("User not found with email: $email")
    }

    fun loginResponse(token: String?) = LoginResponseDto(
        success = token != null,
        message = if (token == null) "Invalid credentials" else "Login succeded",
        token = token
    )
}