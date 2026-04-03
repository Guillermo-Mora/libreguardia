package com.libreguardia.validation

import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.routing.*

fun Route.userValidation() {
    install(RequestValidation) {
        validate<UserCreateDTO> {
            val errors = mutableListOf<String>()
            validateString(it.name)?.let { error -> errors.add(error) }
            validateString(it.surname)?.let { error -> errors.add(error) }
            validatePassword(it.password)?.let { error -> errors.add(error) }
            validateEmail(it.email)?.let { error -> errors.add(error) }
            validatePhoneNumber(it.phoneNumber)?.let { error -> errors.add(error) }
            return@validate validateResult(errors)
        }
        validate<UserEditDTO> {
            val errors = mutableListOf<String>()
            it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
            it.surname?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
            validateNewPassword(
                currentPassword = it.currentPassword,
                newPassword = it.newPassword
            )
            it.currentPassword?.let { field -> validatePassword(field) }?.let { error -> errors.add(error) }
            it.email?.let { field -> validateEmail(field) }?.let { error -> errors.add(error) }
            it.phoneNumber?.let { field -> validatePhoneNumber(field) }?.let { error -> errors.add(error) }
            return@validate validateResult(errors)
        }
    }
}