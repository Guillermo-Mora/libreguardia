package com.libreguardia.validation

import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.routing.*

fun Route.userValidation() {
    install(RequestValidation) {
        validate<UserCreateDTO> {
            val errors = mutableListOf<String>()
            if (it.name.isBlank())
                errors.add("Invalid empty name")
            if (it.surname.isBlank())
                errors.add("Invalid empty surname")
            if (it.userRole.isBlank())
                errors.add("Invalid empty role name")
            if (it.password.length !in 8..50 || it.password.isBlank())
                errors.add("Invalid password length")
            if (!Regex("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matches(it.email))
                errors.add("Invalid email format")
            if (!Regex("^[0-9]{1,20}$").matches(it.phoneNumber))
                errors.add("Phone number can only contain numbers with a length of 1-20")
            if (errors.isNotEmpty()) return@validate ValidationResult.Invalid(errors)
            else return@validate ValidationResult.Valid
        }
        validate<UserEditDTO> {
            val errors = mutableListOf<String>()
            if (it.name != null && it.name.isBlank())
                errors.add("Invalid empty name")
            if (it.surname != null && it.surname.isBlank())
                errors.add("Invalid empty surname")
            if (it.userRole != null && it.userRole.isBlank())
                errors.add("Invalid empty role name")
            if (it.password.length !in 8..50 || it.password.isBlank())
                errors.add("Invalid password length")
            if (!Regex("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matches(it.email))
                errors.add("Invalid email format")
            if (!Regex("^[0-9]{1,20}$").matches(it.phoneNumber))
                errors.add("Phone number can only contain numbers with a length of 1-20")
            if (errors.isNotEmpty()) return@validate ValidationResult.Invalid(errors)
            else return@validate ValidationResult.Valid
        }

}