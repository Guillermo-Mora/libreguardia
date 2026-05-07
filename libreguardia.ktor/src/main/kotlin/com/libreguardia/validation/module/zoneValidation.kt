package com.libreguardia.validation.module

import com.libreguardia.dto.module.ZoneCreateDTO
import com.libreguardia.dto.module.ZoneEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.ZoneCreateField
import com.libreguardia.frontend.component.main.ZoneEditField
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.zoneValidation() {
    validate<ZoneCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.name)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<ZoneEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
}

fun ZoneCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[ZoneCreateField.NAME] = validateString(this.name)
    return errors
}

fun ZoneEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    this.name?.let { name ->
        errors[ZoneEditField.NAME] = validateString(name)
    }
    return errors
}