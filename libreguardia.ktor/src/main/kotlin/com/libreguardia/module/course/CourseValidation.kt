package com.libreguardia.module.course

import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.CourseCreateField
import com.libreguardia.frontend.component.main.edit.CourseEditField
import com.libreguardia.config.validateRequired
import com.libreguardia.config.validateUuid


fun CourseEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[CourseEditField.NAME] = validateRequired(this.name)
    errors[CourseEditField.PROFESSIONAL_FAMILY] = validateUuid(this.professionalFamilyId, true)
    return errors
}

fun CourseCreateDTO.validate():  MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[CourseCreateField.NAME] = validateRequired(this.name)
    errors[CourseCreateField.PROFESSIONAL_FAMILY] = validateUuid(this.professionalFamilyId, true)
    return errors
}