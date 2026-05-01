package com.libreguardia.dto

import com.libreguardia.model.UserModel
import com.libreguardia.util.UUIDSerializer
import com.libreguardia.validation.validateEmail
import com.libreguardia.validation.validateNewPassword
import com.libreguardia.validation.validatePhoneNumber
import com.libreguardia.validation.validateRequired
import com.libreguardia.validation.validateRole
import io.ktor.http.Parameters
import kotlinx.serialization.Serializable
import java.util.UUID

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


//I keep it as Serializable for testing purposes
@Serializable
data class UserEditDTO(
    @Serializable(with = UUIDSerializer::class)
    var id: UUID? = null,
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val isEnabled: Boolean? = null,
    val role: String? = null
)

fun UserEditDTO.validate(): List<String?> {
    val errors = mutableListOf<String?>()
    errors.add(validateRequired(this.name))
    errors.add(validateRequired(this.surname))
    errors.add(validateEmail(this.email, true))
    errors.add(validatePhoneNumber(this.phoneNumber, true))
    errors.add(validateNewPassword(this.password, false))
    errors.add(validateRole(this.role, true))
    //For the enabled/disabled check box
    errors.add(null)
    return errors
}

fun UserEditDTO.toModel(
    userUuid: UUID
): UserModel =
    UserModel(
        id = userUuid,
        name = this.name.toString(),
        surname = this.surname.toString(),
        email = this.email.toString(),
        phoneNumber = this.phoneNumber.toString(),
        isEnabled = this.isEnabled == true,
        isDeleted = false,
        role = this.role.toString()
    )

data class UserEditProfileDTO(
    val phoneNumber: String?,
    val currentPassword: String?,
    val newPassword: String?,
)

fun Parameters.toUserEditDTO() =
    UserEditDTO(
        name = this["name"],
        surname = this["surname"],
        email = this["email"],
        phoneNumber = this["phonenumber"],
        password = this["newpassword"],
        isEnabled = this["enabled"].let { it == "checked" },
        role = this["role"]?.uppercase()
    )

fun Parameters.toUserEditProfileDTO() =
    UserEditProfileDTO(
        phoneNumber = this["phoneNumber"],
        currentPassword = this["currentPassword"],
        newPassword = this["newPassword"]
    )

sealed class EditUserProfileResult {
    data class Success(val userPhoneNumber: String) : EditUserProfileResult()

    data class Error(
        val userEditProfileDTO: UserEditProfileDTO,
        val phoneNumberError: String?,
        val currentPasswordError: String?,
        val newPasswordError: String?
    ) : EditUserProfileResult()
}