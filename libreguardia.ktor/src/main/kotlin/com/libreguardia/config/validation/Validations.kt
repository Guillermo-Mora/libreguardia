package com.libreguardia.config.validation

import kotlinx.datetime.LocalDate
import java.math.BigDecimal
import java.util.*


//Validation functions are currently being reworked to match the new API validations
private const val CONTINUE: String = "__continue__"

fun validateString(field: String): String? = if (field.isBlank()) "Invalid empty field" else null
fun validateNewPassword(
    value: String?
): String? {
    if (value == null) return null
    if (value.length !in 8..50) return "Length must be between 8-50 characters"
    if (value.contains(" ")) return "Can't contain spaces"
    return null
}

fun validateEmail(
    value: String?
): String? {
    if (value == null) return null
    if (!Regex("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
            .matches(value)
    ) return "Invalid format"
    return null
}
fun validatePhoneNumber(
    value: String?
): String? {
    if (value == null) return null
    if (!Regex("^[0-9]{1,20}$").matches(value)) return "Only put numbers"
    return null
}

fun validateDifficulty(
    field: String?,
    required: Boolean
): String? {
    validateRequired(field, required = required).let { if (it != CONTINUE) return it }
    val notNullField = field.toString()
    runCatching { notNullField.toBigDecimal() }
        .getOrElse { return "Difficulty must be a decimal number between 1.0 - 2.0" }
    val bigDecimal = notNullField.toBigDecimal()
    if (bigDecimal !in BigDecimal(1.0)..BigDecimal(2.0)) return "Difficulty must be between 1.0 - 2.0"
    if (bigDecimal.scale() > 1) return "Maximum 1 decimal allowed"
    return null
}

fun validateUuid(
    field: String?,
    required: Boolean
): String? {
    validateRequired(field, required = required).let { if (it != CONTINUE) return it }
    val notNullField = field.toString()
    runCatching { UUID.fromString(notNullField) }
        .getOrElse { return "Invalid selection" }
    return null
}

fun validateAcademicYearDates(
    startDate: LocalDate,
    endDate: LocalDate
): String? {
    return if (startDate >= endDate) "Start date must be before end date" else null
}

private fun validateRequired(
    field: String?,
    required: Boolean,
    blankValidation: Boolean = true
): String? {
    if (blankValidation) {
        if (required && field.isNullOrBlank()) return "Field required"
        if (!required && field.isNullOrBlank()) return null
    } else {
        if (required && field.isNullOrEmpty()) return "Field required"
        if (!required && field.isNullOrEmpty()) return null
    }
    return CONTINUE
}

fun validateRequired(
    field: String?,
    blankValidation: Boolean = true
): String? {
    if (blankValidation) {
        if (field.isNullOrBlank()) return "Field required"
    } else
        if (field.isNullOrEmpty()) return "Field required"
    return null
}

fun validateBlank(
    value: String?
): String? {
    if (value == null) return null
    if (value.isBlank()) return "Field can't be blank"
    return null
}