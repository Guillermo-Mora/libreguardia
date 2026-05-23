package com.libreguardia.module.professional_family

import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.ProfessionalFamilyCreateField
import com.libreguardia.frontend.component.main.edit.ProfessionalFamilyEditField
import com.libreguardia.config.validateRequired

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