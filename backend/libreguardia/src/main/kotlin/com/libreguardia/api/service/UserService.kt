package com.libreguardia.api.service

import com.libreguardia.api.dto.UserCreateRequestDto
import com.libreguardia.api.entity.User
import com.libreguardia.api.exception.UserRoleNotFoundException
import com.libreguardia.api.repository.UserRepository
import com.libreguardia.api.repository.UserRoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository
) {
    fun create(userCreateRequestDto: UserCreateRequestDto) {
        userRoleRepository.findByName(userCreateRequestDto.userRoleName)
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
            } ?: throw UserRoleNotFoundException("Role named ${userCreateRequestDto.userRoleName} doesn't exists")
    }

    //In case an operation makes various queries and all they have to execute in order withouth fail, I should
    // add the @Transactional from SpringFramework, wich will automatically rollack if something fails.
    @Transactional
    private fun testfunction() = "test"
}