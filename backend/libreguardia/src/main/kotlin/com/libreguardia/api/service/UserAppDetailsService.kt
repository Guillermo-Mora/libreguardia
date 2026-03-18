package com.libreguardia.api.service

import com.libreguardia.api.repository.UserRepository
import com.libreguardia.api.security.UserAppDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserAppDetailsService (
    private val userRepository: UserRepository,
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserAppDetails {
        return userRepository.findByEmail(email)
            ?.let {
                UserAppDetails(
                    userEmail = it.email,
                    userIsActive = it.isActive,
                    userPassword = it.password,
                    userRoleName = it.userRole.name,
                )
            }
            ?: throw UsernameNotFoundException("User not found with email: $email")
    }
}