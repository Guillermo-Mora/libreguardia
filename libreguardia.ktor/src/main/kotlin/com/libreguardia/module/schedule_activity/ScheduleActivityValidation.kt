package com.libreguardia.module.schedule_activity

import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.ScheduleActivityCreateField
import com.libreguardia.frontend.component.main.edit.ScheduleActivityEditField
import com.libreguardia.config.validation.validateRequired

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

