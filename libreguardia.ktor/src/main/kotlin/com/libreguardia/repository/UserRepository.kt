package com.libreguardia.repository

import com.libreguardia.db.UserEntity
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.db.UserTable
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.dto.entityToResponse
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID


class UserRepository {
    fun all(): List<UserResponseDTO> = UserEntity.all().map(::entityToResponse)
    fun getByUUID(
        uuid: UUID
    ): UserResponseDTO? {
        val user = UserEntity.find { UserTable.id eq uuid }.firstOrNull()
        return if (user != null) entityToResponse(user) else null
    }
    fun save(
        userCreateDTO: UserCreateDTO,
        userRoleEntity: UserRoleEntity,
        hashedPassword: String
    ) {
        UserEntity.new {
            this.name = userCreateDTO.name
            surname = userCreateDTO.surname
            email = userCreateDTO.email
            phoneNumber = userCreateDTO.phoneNumber
            password = hashedPassword
            isEnabled = userCreateDTO.isEnabled
            userRole = userRoleEntity
        }
    }
}