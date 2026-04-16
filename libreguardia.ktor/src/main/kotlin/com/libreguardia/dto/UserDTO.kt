package com.libreguardia.dto

import com.libreguardia.db.model.UserEntity
import com.libreguardia.repository.UserRepository
import kotlinx.serialization.Serializable

@Serializable
data class UserCreateDTO(
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val isEnabled: Boolean,
    val role: String
)

@Serializable
data class UserEditDTO(
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val isEnabled: Boolean? = null,
    val role: String? = null
)

@Serializable
data class UserEditProfileDTO(
    val phoneNumber: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null,
)
