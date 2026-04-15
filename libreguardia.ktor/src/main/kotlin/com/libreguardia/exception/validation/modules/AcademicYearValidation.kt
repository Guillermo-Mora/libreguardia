package com.libreguardia.exception.validation.modules

import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.academicYearValidation() {
    validate<AcademicYearCreateDTO> { dto ->
        when {
            dto.name.isBlank() -> ValidationResult.Invalid("Name cannot be blank")
            dto.startDate > dto.endDate -> ValidationResult.Invalid("Start date must be before end date")
            else -> ValidationResult.Valid
        }
    }
    validate<AcademicYearEditDTO> { dto ->
        when {
            dto.name != null && dto.name.isBlank() -> ValidationResult.Invalid("Name cannot be blank")
            dto.startDate != null && dto.endDate != null && dto.startDate > dto.endDate -> 
                ValidationResult.Invalid("Start date must be before end date")
            else -> ValidationResult.Valid
        }
    }
}
