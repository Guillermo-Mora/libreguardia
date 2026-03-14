package com.libreguardia.api.service

import com.libreguardia.api.entity.User
import com.libreguardia.api.entity.UserRole
import com.libreguardia.api.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun createUser(
        name: String,
        surname: String,
        email: String,
        phoneNumber: String,
        rawPassword: String,
        userRole: UserRole
    ): User {
        val user = User().apply {
            this.name = name
            this.surname = surname
            this.email = email
            this.phoneNumber = phoneNumber
            this.password = passwordEncoder.encode(rawPassword)!!
            this.userRole = userRole
        }
        return userRepository.save(user)
    }
}