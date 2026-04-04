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
    @Serializable(with = UUIDSerializer::class)
    val userRoleUUID: UUID
)

@Serializable
data class UserEditDTO(
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val isEnabled: Boolean? = null,
    @Serializable(with = UUIDSerializer::class)
    val userRoleUUID: UUID? = null
)

@Serializable
data class UserEditProfileDTO(
    val phoneNumber: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null,
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