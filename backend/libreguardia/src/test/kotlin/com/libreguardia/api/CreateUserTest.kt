package com.libreguardia.api

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
        val userRole = userRoleRepository.findByName("admin")!!

        userService.createUser(
            name = "admin",
            surname = "admin",
            email = "admin@admin.com",
            phoneNumber = "000000000",
            rawPassword = "admin",
            userRole = userRole
        )
    }
}