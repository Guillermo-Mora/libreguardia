package com.libreguardia.validation.module

import com.libreguardia.dto.module.BuildingCreateDTO
import com.libreguardia.dto.module.BuildingEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.BuildingCreateField
import com.libreguardia.frontend.component.main.edit.BuildingEditField
import com.libreguardia.validation.*
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

fun BuildingCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[BuildingCreateField.NAME] = validateRequired(this.name)
    return errors
}

fun BuildingEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[BuildingEditField.NAME] = validateRequired(this.name)
    return errors
}