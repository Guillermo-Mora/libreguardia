package com.libreguardia.validation.modules

import com.libreguardia.dto.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.ProfessionalFamilyEditDTO
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.professionalFamilyValidation() {
    validate<ProfessionalFamilyCreateDTO> {
        val errors = mutableListOf<String>()
        it.name.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<ProfessionalFamilyEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
}