package com.libreguardia.exception.validation.modules

import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.exception.validation.validateAcademicYearDates
import com.libreguardia.exception.validation.validateAcademicYearName
import com.libreguardia.exception.validation.validateResult
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.academicYearValidation() {
    validate<AcademicYearCreateDTO> {
        val errors = mutableListOf<String>()
        validateAcademicYearName(it.name)?.let { error -> errors.add(error) }
        validateAcademicYearDates(it.startDate, it.endDate)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<AcademicYearEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateAcademicYearName(field) }?.let { error -> errors.add(error) }
        it.startDate?.let { start ->
            it.endDate?.let { end ->
                validateAcademicYearDates(start, end)?.let { error -> errors.add(error) }
            }
        }
        return@validate validateResult(errors)
    }
}
