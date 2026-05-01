package com.libreguardia.dto

import com.libreguardia.db.model.UserEntity
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.repository.UserRepository
import com.libreguardia.validation.validatePhoneNumber
import io.ktor.http.Parameters
import io.ktor.http.parameters
import kotlinx.serialization.Serializable
import java.lang.reflect.Parameter

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

data class UserEditProfileDTO(
    val phoneNumber: String?,
    val currentPassword: String?,
    val newPassword: String?,
)

fun Parameters.toUserEditProfileDTO() =
    UserEditProfileDTO(
        phoneNumber = this["phoneNumber"],
        currentPassword = this["currentPassword"],
        newPassword = this["newPassword"]
    )

sealed class EditProfileResult {
    data class Success(val userPhoneNumber: String) : EditProfileResult()

    data class Error(
        val userEditProfileDTO: UserEditProfileDTO,
        val phoneNumberError: String?,
        val currentPasswordError: String?,
        val newPasswordError: String?
    ) : EditProfileResult()
}