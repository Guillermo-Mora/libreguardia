package com.libreguardia.validation.module

import com.libreguardia.dto.module.CourseCreateDTO
import com.libreguardia.dto.module.CourseEditDTO
import com.libreguardia.dto.module.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.module.ProfessionalFamilyEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.CourseCreateField
import com.libreguardia.frontend.component.main.CourseEditField
import com.libreguardia.frontend.component.main.ProfessionalFamilyCreateField
import com.libreguardia.frontend.component.main.ProfessionalFamilyEditField
import com.libreguardia.validation.validateRequired
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*


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