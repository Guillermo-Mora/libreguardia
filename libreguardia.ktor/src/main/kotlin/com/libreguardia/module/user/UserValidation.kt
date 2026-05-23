package com.libreguardia.module.user

import com.libreguardia.module.shared.validation.validateBlank
import com.libreguardia.module.shared.validation.validateEmail
import com.libreguardia.module.shared.validation.validateNewPassword
import com.libreguardia.module.shared.validation.validatePhoneNumber
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.UserCreateField
import com.libreguardia.frontend.component.main.edit.UserEditField

fun UserEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[UserEditField.NAME] = validateBlank(this.name)
    errors[UserEditField.SURNAME] = validateBlank(this.surname)
    errors[UserEditField.EMAIL] = validateEmail(this.email)
    errors[UserEditField.PHONE_NUMBER] = validatePhoneNumber(this.phoneNumber)
    errors[UserEditField.NEW_PASSWORD] = validateNewPassword(this.password)
    return errors
}

fun UserCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[UserCreateField.NAME] = validateBlank(this.name)
    errors[UserCreateField.SURNAME] = validateBlank(this.surname)
    errors[UserCreateField.EMAIL] = validateEmail(this.email)
    errors[UserCreateField.PHONE_NUMBER] = validatePhoneNumber(this.phoneNumber)
    errors[UserCreateField.NEW_PASSWORD] = validateNewPassword(this.password)
    return errors
}