package com.libreguardia.validation

import com.libreguardia.dto.LoginDTO
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.authValidation() {
    validate<LoginDTO> {
        val errors = mutableListOf<String>()
        validateString(it.email)?.let { error -> errors.add(error) }
        validateString(it.password)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
}