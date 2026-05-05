package com.libreguardia.dto

import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.UserCreateField
import com.libreguardia.frontend.component.main.UserEditField
import io.ktor.http.*

data class UserCreateDTO(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val isEnabled: Boolean = true,
    val role: String = ""
)

data class UserEditDTO(
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val isEnabled: Boolean,
    val role: String
)

//Helpers for receiving parameters
fun Parameters.string(field: FormField) = this[field.id] ?: ""
fun Parameters.boolean(field: FormField) = this[field.id].let { it == "checked" }
fun Parameters.enum(field: FormField) = this[field.id]?.uppercase() ?: ""

fun Parameters.toUserEditDTO() =
    UserEditDTO(
        name = string(UserEditField.NAME),
        surname = string(UserEditField.SURNAME),
        email = string(UserEditField.EMAIL),
        phoneNumber = string(UserEditField.PHONE_NUMBER),
        password = string(UserEditField.NEW_PASSWORD),
        isEnabled = boolean(UserEditField.ENABLED),
        role = enum(UserEditField.ROLE)
    )

fun Parameters.toUserCreateDTO() =
    UserCreateDTO(
        name = string(UserCreateField.NAME),
        surname = string(UserCreateField.SURNAME),
        email = string(UserCreateField.EMAIL),
        phoneNumber = string(UserCreateField.PHONE_NUMBER),
        password = string(UserCreateField.NEW_PASSWORD),
        isEnabled = boolean(UserCreateField.ENABLED),
        role = enum(UserCreateField.ROLE)
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