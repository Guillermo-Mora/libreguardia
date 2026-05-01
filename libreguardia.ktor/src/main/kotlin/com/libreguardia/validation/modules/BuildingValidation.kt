package com.libreguardia.validation.modules

import com.libreguardia.dto.BuildingCreateDTO
import com.libreguardia.dto.BuildingEditDTO
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.buildingValidation() {
    validate<BuildingCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.name)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<BuildingEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
}