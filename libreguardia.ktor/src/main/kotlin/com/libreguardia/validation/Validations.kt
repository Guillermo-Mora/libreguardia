package com.libreguardia.validation

import com.libreguardia.db.Role
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.datetime.LocalDate

private const val CONTINUE: String = "__continue__"
fun validateResult(errors: List<String>): ValidationResult =
    if (errors.isNotEmpty()) ValidationResult.Invalid(errors)
    else ValidationResult.Valid
fun validateString(field: String): String? = if (field.isBlank()) "Invalid empty field" else null
fun validateNewPassword(
    field: String?,
    required: Boolean
): String? {
    validateRequired(field, required = required).let { if (it != CONTINUE) return it }
    val notNullField = field.toString()
    if (notNullField.length !in 8..50) return "Length should be between 8-50"
    return null
}
/*
fun validateNewPassword(
    currentPassword: String?,
    newPassword: String?
): String? =
    if (currentPassword.isNullOrBlank() || newPassword.isNullOrBlank()) "Invalid empty password"
    else validatePassword(newPassword)

 */
fun validateEmail(
    field: String?,
    required: Boolean
): String? {
    validateRequired(field, required = required).let { if (it != CONTINUE) return it }
    val notNullField = field.toString()
    if (!Regex("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
            .matches(notNullField)
    ) return "Invalid email format"
    return null
}
fun validatePhoneNumber(
    field: String?,
    required: Boolean
): String? {
    validateRequired(field, required =required).let { if (it != CONTINUE) return it }
    val notNullField = field.toString()
    if (!Regex("^[0-9]{1,20}$").matches(notNullField)) return "Invalid phone number format"
    return null
}
fun validateRole(
    field: String?,
    required: Boolean
): String? {
    validateRequired(field, required = required).let { if (it != CONTINUE) return it }
    if (Role.entries.firstOrNull { it.name == field } == null) return "Invalid role"
    return null
}
fun validateRefreshToken(field: String) : String? =
    if (field.length != 32) "Invalid refresh token" else null
fun validateDecimal(field: Double): String? =
    if (field !in 0.0..9.9) "Invalid decimal value (must be between 0.0 and 9.9)" else null
fun validateAcademicYearDates(startDate: kotlinx.datetime.LocalDate, endDate: kotlinx.datetime.LocalDate): String? =
    if (startDate > endDate) "Start date must be before end date" else null

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