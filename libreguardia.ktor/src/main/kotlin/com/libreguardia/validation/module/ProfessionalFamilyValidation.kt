package com.libreguardia.validation.module

import com.libreguardia.dto.module.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.module.ProfessionalFamilyEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.ProfessionalFamilyCreateField
import com.libreguardia.frontend.component.main.ProfessionalFamilyEditField
import com.libreguardia.validation.validateRequired

fun ProfessionalFamilyEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[ProfessionalFamilyEditField.NAME] = validateRequired(this.name)
    return errors
}

fun ProfessionalFamilyCreateDTO.validate():  MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[ProfessionalFamilyCreateField.NAME] = validateRequired(this.name)
    return errors
}