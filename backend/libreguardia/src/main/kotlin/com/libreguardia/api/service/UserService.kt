package com.libreguardia.api.service

import com.libreguardia.api.dto.UserCreateRequestDto
import com.libreguardia.api.entity.User
import com.libreguardia.api.repository.UserRepository
import com.libreguardia.api.repository.UserRoleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository
) {
    fun create(userCreateRequestDto: UserCreateRequestDto): ResponseEntity<Map<String, String>> {
        return userRoleRepository.findByName(userCreateRequestDto.userRoleName)
            ?.let {
                userRepository.save(User().apply {
                    name = userCreateRequestDto.name
                    surname = userCreateRequestDto.surname
                    email = userCreateRequestDto.email
                    phoneNumber = userCreateRequestDto.phoneNumber
                    isActive = userCreateRequestDto.isActive
                    //Password encryption still to implement
                    password = userCreateRequestDto.password
                    userRole = it
                })
                createResponseEntity(
                    httpStatus = HttpStatus.CREATED,
                    message = "User created succesfully"
                )
            } ?: createResponseEntity(
            httpStatus = HttpStatus.BAD_REQUEST,
            message = "Selected role doesn't exists"
        )
    }

    //In case an operation makes various queries and all they have to execute in order withouth fail, I should
    // add the @Transactional from SpringFramework, wich will automatically rollack if something fails.
    @Transactional
    private fun testfunction() = "test"

    private fun createResponseEntity(httpStatus: HttpStatus, message: String) =
        ResponseEntity.status(httpStatus).body(mapOf("message" to message))
}