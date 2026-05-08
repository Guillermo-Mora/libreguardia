package com.libreguardia.validation.module

import com.libreguardia.dto.module.ScheduleActivityCreateDTO
import com.libreguardia.dto.module.ScheduleActivityEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.ScheduleActivityCreateField
import com.libreguardia.frontend.component.main.ScheduleActivityEditField
import com.libreguardia.validation.validateRequired

fun ScheduleActivityCreateDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[ScheduleActivityCreateField.NAME] = validateRequired(this.name)
    return errors
}

fun ScheduleActivityEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[ScheduleActivityEditField.NAME] = validateRequired(this.name)
    return errors
}

