package com.libreguardia.api.controller

import com.libreguardia.api.dto.UserCreateRequestDto
import com.libreguardia.api.service.UserService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
//Only required for debugging with Swagger
@SecurityRequirement(name = "bearerAuth")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/create")
    fun createUser(@RequestBody userCreateRequestDto: UserCreateRequestDto): ResponseEntity<Map<String, String>> {
        userService.create(userCreateRequestDto)
        return createResponseEntity(
            httpStatus = HttpStatus.CREATED,
            message = "User created successfully"
        )
    }

    private fun createResponseEntity(
        httpStatus: HttpStatus,
        message: String
    ) = ResponseEntity.status(httpStatus).body(mapOf("message" to message))
}