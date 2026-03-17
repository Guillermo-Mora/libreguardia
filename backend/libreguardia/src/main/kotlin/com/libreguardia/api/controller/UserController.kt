package com.libreguardia.api.controller

import com.libreguardia.api.dto.UserCreateRequestDto
import com.libreguardia.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/create")
    fun createUser(@RequestBody userCreateRequestDto: UserCreateRequestDto): ResponseEntity<Map<String, String>> {
        userService.create(userCreateRequestDto)
        return createResponseEntity(
            httpStatus = HttpStatus.CREATED,
            message = "User created succesfully"
        )
    }

    private fun createResponseEntity(
        httpStatus: HttpStatus,
        message: String
    ) = ResponseEntity.status(httpStatus).body(mapOf("message" to message))
}