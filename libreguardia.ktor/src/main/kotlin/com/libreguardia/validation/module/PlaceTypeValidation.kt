package com.libreguardia.validation.module

import com.libreguardia.dto.module.PlaceTypeCreateDTO
import com.libreguardia.dto.module.PlaceTypeEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.PlaceTypeCreateField
import com.libreguardia.frontend.component.main.edit.PlaceTypeEditField
import com.libreguardia.validation.*
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

fun PlaceTypeCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[PlaceTypeCreateField.NAME] = validateRequired(this.name)
    return errors
}

fun PlaceTypeEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[PlaceTypeEditField.NAME] = validateRequired(this.name)
    return errors
}