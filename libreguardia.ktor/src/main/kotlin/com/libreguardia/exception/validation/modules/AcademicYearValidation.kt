package com.libreguardia.exception.validation.modules

import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.exception.validation.validateAcademicYearDates
import com.libreguardia.exception.validation.validateAcademicYearName
import com.libreguardia.exception.validation.validateResult
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.academicYearValidation() {
    validate<AcademicYearCreateDTO> { dto ->
        val errors = mutableListOf<String>()
        validateAcademicYearName(dto.name)?.let { errors.add(it) }
        validateAcademicYearDates(dto.startDate, dto.endDate)?.let { errors.add(it) }
        validateResult(errors)
    }
    validate<AcademicYearEditDTO> { dto ->
        val errors = mutableListOf<String>()
        dto.name?.let { validateAcademicYearName(it) }?.let { errors.add(it) }
        dto.startDate?.let { start ->
            dto.endDate?.let { end ->
                validateAcademicYearDates(start, end)?.let { errors.add(it) }
            }
        }
        validateResult(errors)
    }
}
