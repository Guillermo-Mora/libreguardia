package com.libreguardia.dto

import com.libreguardia.config.UUIDSerializer
import com.libreguardia.db.UserEntity
import com.libreguardia.repository.UserRepository
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserResponseDTO(
    //@Serializable(with = UUIDSerializer::class)
    //val id: UUID,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val isEnabled: Boolean,
    val isDeleted: Boolean,
    val userRole: String
)

@Serializable
data class UserRequestDTO(
    //@Serializable(with = UUIDSerializer::class)
    //val id: UUID,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val isEnabled: Boolean,
    val userRole: String
)

fun UserRepository.entityToResponse(
    entity: UserEntity
) = UserResponseDTO(
    name = entity.name,
    surname = entity.surname,
    email =entity.email,
    phoneNumber = entity.phoneNumber,
    password = entity.password,
    isEnabled = entity.isEnabled,
    isDeleted = entity.isDeleted,
    userRole = entity.userRole.name
)