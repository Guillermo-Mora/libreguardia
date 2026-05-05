package com.libreguardia.validation.module

import com.libreguardia.dto.module.GroupCreateDTO
import com.libreguardia.dto.module.GroupEditDTO
import com.libreguardia.validation.validateDecimal
import com.libreguardia.validation.validateString
import com.libreguardia.validation.validateResult
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.groupValidation() {
    validate<GroupCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.code)?.let { error -> errors.add(error) }
        it.pointsMultiplier?.let { multiplier ->
            validateDecimal(multiplier)?.let { error -> errors.add(error) }
        }
        return@validate validateResult(errors)
    }
    validate<GroupEditDTO> {
        val errors = mutableListOf<String>()
        it.code?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        it.pointsMultiplier?.let { multiplier ->
            validateDecimal(multiplier)?.let { error -> errors.add(error) }
        }
        return@validate validateResult(errors)
    }
}
