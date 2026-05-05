package com.libreguardia.validation.module

import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
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
