package com.libreguardia.validation.modules

import com.libreguardia.dto.ScheduleActivityCreateDTO
import com.libreguardia.dto.ScheduleActivityEditDTO
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.scheduleActivityValidation() {
    validate<ScheduleActivityCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.name)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<ScheduleActivityEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
}

