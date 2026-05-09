package com.libreguardia.api

import com.libreguardia.api.dto.UserCreateRequestDto
import com.libreguardia.api.repository.UserRoleRepository
import com.libreguardia.api.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreateUserTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRoleRepository: UserRoleRepository

    @Test
    fun `should create user`() {
        userService.create(
            UserCreateRequestDto(
                name = "user",
                surname = "user",
                email = "user@user.com",
                phoneNumber = "000",
                active = true,
                password = "123",
                userRoleName = ""
            )
        )
    }
}