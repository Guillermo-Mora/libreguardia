package com.libreguardia.validation.module

import com.libreguardia.dto.module.ZoneCreateDTO
import com.libreguardia.dto.module.ZoneEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.ZoneCreateField
import com.libreguardia.frontend.component.main.ZoneEditField
import com.libreguardia.validation.validateRequired

fun ZoneCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[ZoneCreateField.NAME] = validateRequired(this.name)
    return errors
}

fun ZoneEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[ZoneEditField.NAME] = validateRequired(this.name)
    return errors
}