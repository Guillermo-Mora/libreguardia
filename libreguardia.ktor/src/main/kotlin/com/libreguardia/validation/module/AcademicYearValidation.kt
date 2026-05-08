package com.libreguardia.validation.module

import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.AcademicYearCreateField
import com.libreguardia.frontend.component.main.AcademicYearEditField
import com.libreguardia.validation.validateAcademicYearDates
import com.libreguardia.validation.validateString

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
