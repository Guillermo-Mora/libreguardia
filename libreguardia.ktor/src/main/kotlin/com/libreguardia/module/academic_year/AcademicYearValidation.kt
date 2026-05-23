package com.libreguardia.module.academic_year

import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.AcademicYearCreateField
import com.libreguardia.frontend.component.main.edit.AcademicYearEditField
import com.libreguardia.config.validateAcademicYearDates
import com.libreguardia.config.validateBlank

fun AcademicYearCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[AcademicYearCreateField.NAME] = validateBlank(this.name)
    errors[AcademicYearCreateField.START_DATE] = validateAcademicYearDates(
        startDate = this.startDate,
        endDate = this.endDate
    )
    return errors
}

fun AcademicYearEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[AcademicYearEditField.NAME] = validateBlank(this.name)
    errors[AcademicYearEditField.START_DATE] = validateAcademicYearDates(
        startDate = this.startDate,
        endDate = this.endDate
    )
    return errors
}