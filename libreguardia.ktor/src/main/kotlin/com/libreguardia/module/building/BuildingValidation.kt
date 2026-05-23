package com.libreguardia.module.building

import com.libreguardia.module.shared.validation.validateRequired
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.BuildingCreateField
import com.libreguardia.frontend.component.main.edit.BuildingEditField

fun BuildingCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[BuildingCreateField.NAME] = validateRequired(this.name)
    return errors
}

fun BuildingEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[BuildingEditField.NAME] = validateRequired(this.name)
    return errors
}