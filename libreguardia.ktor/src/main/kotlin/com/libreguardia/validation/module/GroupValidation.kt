package com.libreguardia.validation.module

import com.libreguardia.dto.module.GroupCreateDTO
import com.libreguardia.dto.module.GroupEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.GroupCreateField
import com.libreguardia.frontend.component.main.edit.GroupEditField
import com.libreguardia.validation.validateDifficulty
import com.libreguardia.validation.validateRequired
import com.libreguardia.validation.validateUuid


fun GroupEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[GroupEditField.CODE] = validateRequired(this.code)
    errors[GroupEditField.POINTS_MULTIPLIER] = validateDifficulty(this.pointsMultiplier, true)
    errors[GroupEditField.COURSE] = validateUuid(this.courseId, true)
    return errors
}

fun GroupCreateDTO.validate():  MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[GroupCreateField.CODE] = validateRequired(this.code)
    errors[GroupCreateField.POINTS_MULTIPLIER] = validateDifficulty(this.pointsMultiplier, true)
    errors[GroupCreateField.COURSE] = validateUuid(this.courseId, true)
    return errors
}