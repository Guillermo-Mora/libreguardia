package com.libreguardia.module.zone

import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.ZoneCreateField
import com.libreguardia.frontend.component.main.ZoneEditField
import com.libreguardia.config.validateRequired

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