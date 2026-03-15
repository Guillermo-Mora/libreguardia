package com.libreguardia.api.service

import com.libreguardia.api.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findByEmail(email)
            ?.let {
                User(
                    it.email,
                    it.password,
                    listOf(SimpleGrantedAuthority(it.userRole.name))
                )
            }
            ?: throw UsernameNotFoundException("User not found with email: $email")
    }

    fun addUser(user: com.libreguardia.api.entity.User): String {
        user.password = passwordEncoder.encode(user.password)!!
        userRepository.save(user)
        return "User added successfully!"
    }
}