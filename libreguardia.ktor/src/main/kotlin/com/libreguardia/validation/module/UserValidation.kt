package com.libreguardia.validation.module

import com.libreguardia.dto.module.UserCreateDTO
import com.libreguardia.dto.module.UserEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.UserCreateField
import com.libreguardia.frontend.component.main.edit.UserEditField
import com.libreguardia.validation.*

fun UserEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[UserEditField.NAME] = validateRequired(this.name)
    errors[UserEditField.SURNAME] = validateRequired(this.surname)
    errors[UserEditField.EMAIL] = validateEmail(this.email, true)
    errors[UserEditField.PHONE_NUMBER] = validatePhoneNumber(this.phoneNumber, true)
    errors[UserEditField.NEW_PASSWORD] = validateNewPassword(this.password, false)
    errors[UserEditField.ROLE] = validateRole(this.role, true)
    return errors
}

fun UserCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[UserCreateField.NAME] = validateRequired(this.name)
    errors[UserCreateField.SURNAME] = validateRequired(this.surname)
    errors[UserCreateField.EMAIL] = validateEmail(this.email, true)
    errors[UserCreateField.PHONE_NUMBER] = validatePhoneNumber(this.phoneNumber, true)
    errors[UserCreateField.NEW_PASSWORD] = validateNewPassword(this.password, false)
    errors[UserCreateField.ROLE] = validateRole(this.role, true)
    return errors
}