package com.libreguardia.repository

import com.libreguardia.db.UserEntity
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.dto.UserRequestDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.dto.entityToResponse


class UserRepository {
    suspend fun all(): List<UserResponseDTO> = UserEntity.all().map(::entityToResponse)
    suspend fun save(
        userRequestDTO: UserRequestDTO,
        userRoleEntity: UserRoleEntity,
        hashedPassword: String
    ) {
        UserEntity.new {
            this.name = userRequestDTO.name
            surname = userRequestDTO.surname
            email = userRequestDTO.email
            phoneNumber = userRequestDTO.phoneNumber
            password = hashedPassword
            isEnabled = userRequestDTO.isEnabled
            userRole = userRoleEntity
        }
    }
}