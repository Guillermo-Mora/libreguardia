package com.libreguardia.validation.modules

import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.validation.validateEmail

import com.libreguardia.validation.validatePhoneNumber
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateRole
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.userValidation() {
    validate<UserCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.name)?.let { error -> errors.add(error) }
        validateString(it.surname)?.let { error -> errors.add(error) }
        //validatePassword(it.password)?.let { error -> errors.add(error) }
        validateEmail(it.email)?.let { error -> errors.add(error) }
        validatePhoneNumber(it.phoneNumber)?.let { error -> errors.add(error) }
        validateRole(it.role)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<UserEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        it.surname?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        //it.password?.let { field -> validatePassword(field) }?.let { error -> errors.add(error) }
        it.email?.let { field -> validateEmail(field) }?.let { error -> errors.add(error) }
        it.phoneNumber?.let { field -> validatePhoneNumber(field) }?.let { error -> errors.add(error) }
        it.role?.let { field -> validateRole(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<UserEditProfileDTO> {
        val errors = mutableListOf<String>()
        it.phoneNumber?.let { field -> validatePhoneNumber(field) }?.let { error -> errors.add(error) }
        /*
        validateNewPassword(
            currentPassword = it.currentPassword,
            newPassword = it.newPassword
        )?.let { error -> errors.add(error) }
         */
        return@validate validateResult(errors)
    }
}