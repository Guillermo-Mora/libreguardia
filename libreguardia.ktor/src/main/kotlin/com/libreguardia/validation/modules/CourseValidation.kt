package com.libreguardia.validation.modules

import com.libreguardia.dto.CourseCreateDTO
import com.libreguardia.dto.CourseEditDTO
import com.libreguardia.validation.validateResult
import com.libreguardia.validation.validateString
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.courseValidation() {
    validate<CourseCreateDTO> {
        val errors = mutableListOf<String>()
        validateString(it.name)?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
    validate<CourseEditDTO> {
        val errors = mutableListOf<String>()
        it.name?.let { field -> validateString(field) }?.let { error -> errors.add(error) }
        return@validate validateResult(errors)
    }
}