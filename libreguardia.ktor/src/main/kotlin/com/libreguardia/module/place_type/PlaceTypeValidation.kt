package com.libreguardia.module.place_type

import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.PlaceTypeCreateField
import com.libreguardia.frontend.component.main.edit.PlaceTypeEditField
import com.libreguardia.config.validation.validateRequired

fun PlaceTypeCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[PlaceTypeCreateField.NAME] = validateRequired(this.name)
    return errors
}

fun PlaceTypeEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[PlaceTypeEditField.NAME] = validateRequired(this.name)
    return errors
}