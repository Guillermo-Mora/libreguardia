package com.libreguardia.validation.modules

import com.libreguardia.dto.PlaceTypeCreateDTO
import com.libreguardia.dto.PlaceTypeEditDTO
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.placeTypeValidation() {
    validate<PlaceTypeCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.name)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<PlaceTypeEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
}