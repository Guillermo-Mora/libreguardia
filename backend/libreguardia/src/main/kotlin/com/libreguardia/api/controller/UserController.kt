package com.libreguardia.api.controller

import com.libreguardia.api.dto.AuthRequestDTO
import com.libreguardia.api.entity.User
import com.libreguardia.api.service.JwtService
import com.libreguardia.api.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class UserController (
    private val userService: UserService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    @GetMapping("/welcome")
    fun welcome(): String {
        return "Welcome this endpoint is not secure"
    }

    @PostMapping("/addNewUser")
    fun addNewUser(@RequestBody user: User): String {
        return userService.addUser(user)
    }

    @PostMapping("/generateToken")
    fun authenticateAndGetToken(@RequestBody authRequest: AuthRequestDTO): String {
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    authRequest.email,
                    authRequest.password
                )
            )
            println("Authentication result: ${authentication.isAuthenticated}")
            return if (authentication.isAuthenticated) jwtService.generateToken(authRequest.email)
            else throw UsernameNotFoundException("Invalid user request!")
        } catch (e: Exception) {
            println("Authentication failed: ${e.javaClass.name} - ${e.message}")
            throw e
        }
    }
}