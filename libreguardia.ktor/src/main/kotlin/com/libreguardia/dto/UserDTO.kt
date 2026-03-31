package com.libreguardia.dto

import com.libreguardia.config.UUIDSerializer
import com.libreguardia.db.UserEntity
import com.libreguardia.repository.UserRepository
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val isEnabled: Boolean,
    val isDeleted: Boolean,
    val userRole: String
)

@Serializable
data class UserCreateDTO(
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val isEnabled: Boolean,
    val userRole: String
)

@Serializable
data class UserEditDTO(
    val name: String?,
    val surname: String?,
    val email: String?,
    val phoneNumber: String?,
    val password: String?,
    val newPassword: String?,
    val isEnabled: Boolean?,
    val userRole: String?
)

fun UserRepository.entityToResponse(
    entity: UserEntity
) = UserResponseDTO(
    id = entity.id.value,
    name = entity.name,
    surname = entity.surname,
    email =entity.email,
    phoneNumber = entity.phoneNumber,
    isEnabled = entity.isEnabled,
    isDeleted = entity.isDeleted,
    userRole = entity.userRole.name
)