package com.libreguardia.api

import com.libreguardia.api.entity.User
import com.libreguardia.api.repository.UserRoleRepository
import com.libreguardia.api.service.UserAppDetailsService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import kotlin.apply

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreateUserTest {

    @Autowired
    private lateinit var userAppDetailsService: UserAppDetailsService

    @Autowired
    private lateinit var userRoleRepository: UserRoleRepository

    @Test
    fun `should create user`() {
        userAppDetailsService.addUser(
            User().apply {
                this.name = "admin"
                this.surname = "admin"
                this.email = "admin@admin.com"
                this.phoneNumber = "000000000"
                this.password = "admin"
                this.userRole = userRoleRepository.findByName("ROLE_ADMIN")!!
            }
        )
    }
}