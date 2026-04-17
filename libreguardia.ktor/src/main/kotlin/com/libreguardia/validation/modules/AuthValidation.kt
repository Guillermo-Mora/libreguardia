package com.libreguardia.validation.modules

import com.libreguardia.dto.LoginDTO
import com.libreguardia.dto.RefreshTokenDTO
import com.libreguardia.validation.validateRefreshToken
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.authValidation() {
    validate<LoginDTO> {
        val errors = mutableListOf<String>()
        validateString(it.email)?.let { error -> errors.add(error) }
        validateString(it.password)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<RefreshTokenDTO> {
        validateRefreshToken(it.refreshToken)?.let { error -> return@validate ValidationResult.Invalid(error) }
        return@validate ValidationResult.Valid
    }
}