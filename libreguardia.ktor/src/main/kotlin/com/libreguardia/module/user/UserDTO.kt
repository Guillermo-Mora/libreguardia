package com.libreguardia.module.user

import com.libreguardia.config.database.Role
import com.libreguardia.config.validation.get
import com.libreguardia.config.validation.getOrNull
import com.libreguardia.frontend.component.main.create.UserCreateField
import com.libreguardia.frontend.component.main.edit.UserEditField
import io.ktor.http.*

data class UserCreateDTO(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val isEnabled: Boolean = true,
    val role: Role = Role.USER
)

data class UserEditDTO(
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val password: String?,
    val isEnabled: Boolean,
    val role: Role
)

fun Parameters.toUserEditDTO() =
    UserEditDTO(
        name = get<String>(UserEditField.NAME),
        surname = get<String>(UserEditField.SURNAME),
        email = get<String>(UserEditField.EMAIL),
        phoneNumber = get<String>(UserEditField.PHONE_NUMBER),
        password = getOrNull<String>(UserEditField.NEW_PASSWORD),
        isEnabled = get<Boolean>(UserEditField.ENABLED),
        role = get<Role>(UserEditField.ROLE)
    )

fun Parameters.toUserCreateDTO() =
    UserCreateDTO(
        name = get<String>(UserCreateField.NAME),
        surname = get<String>(UserCreateField.SURNAME),
        email = get<String>(UserCreateField.EMAIL),
        phoneNumber = get<String>(UserCreateField.PHONE_NUMBER),
        password = get<String>(UserCreateField.NEW_PASSWORD),
        isEnabled = get<Boolean>(UserCreateField.ENABLED),
        role = get<Role>(UserCreateField.ROLE)
    )

fun UserModel.toUserEditDTO(): UserEditDTO =
    UserEditDTO(
        name = this.name,
        surname = this.surname,
        email = this.email,
        phoneNumber = this.phoneNumber,
        password = "",
        isEnabled = this.isEnabled,
        role = this.role
    )







//This has to be reworked to use the new way of doing it instead of this
//
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

sealed class EditUserProfileResult {
    data class Success(val userPhoneNumber: String) : EditUserProfileResult()

    data class Error(
        val userEditProfileDTO: UserEditProfileDTO,
        val phoneNumberError: String?,
        val currentPasswordError: String?,
        val newPasswordError: String?
    ) : EditUserProfileResult()
}
//
//