package com.libreguardia.validation.module

import com.libreguardia.dto.module.CourseCreateDTO
import com.libreguardia.dto.module.CourseEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.CourseCreateField
import com.libreguardia.frontend.component.main.edit.CourseEditField
import com.libreguardia.validation.validateRequired


fun CourseEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[CourseEditField.NAME] = validateRequired(this.name)
    errors[CourseEditField.PROFESSIONAL_FAMILY] = validateRequired(this.professionalFamilyId.toString())
    return errors
}

fun CourseCreateDTO.validate():  MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[CourseCreateField.NAME] = validateRequired(this.name)
    errors[CourseCreateField.PROFESSIONAL_FAMILY] = validateRequired(this.professionalFamilyId.toString())
    return errors
}