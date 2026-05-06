package com.libreguardia.validation.module

import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.AcademicYearCreateField
import com.libreguardia.frontend.component.main.AcademicYearEditField
import com.libreguardia.validation.validateAcademicYearDates
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.academicYearValidation() {
    validate<AcademicYearCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.name)?.let { error -> errors.add(error) }
        validateAcademicYearDates(it.startDate, it.endDate)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<AcademicYearEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        it.startDate?.let { start ->
            it.endDate?.let { end ->
                validateAcademicYearDates(start, end)?.let { error -> errors.add(error) }
            }
        }
        return@validate validateResult(errors)
    }
}

fun AcademicYearCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[AcademicYearCreateField.NAME] = validateString(this.name)
    validateAcademicYearDates(this.startDate, this.endDate)?.let { error ->
        errors[AcademicYearCreateField.START_DATE] = error
    }
    return errors
}

fun AcademicYearEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    this.name?.let { name ->
        errors[AcademicYearEditField.NAME] = validateString(name)
    }
    if (this.startDate != null && this.endDate != null) {
        validateAcademicYearDates(this.startDate, this.endDate)?.let { error ->
            errors[AcademicYearEditField.START_DATE] = error
        }
    }
    return errors
}
