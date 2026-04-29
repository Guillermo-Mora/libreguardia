package com.libreguardia.validation

import com.libreguardia.db.Role
import io.ktor.server.plugins.requestvalidation.*

fun validateResult(errors: List<String>): ValidationResult =
    if (errors.isNotEmpty()) ValidationResult.Invalid(errors)
    else ValidationResult.Valid
fun validateString(field: String): String? = if (field.isBlank()) "Invalid empty field" else null
fun validateNewPassword(
    field: String?,
    required: Boolean
): String? {
    return if (!required && field.isNullOrEmpty()) null
    else if (field.isNullOrEmpty()) "Invalid empty field"
    else if (field.length !in 8..50) "Length should be between 8-50" else null
}
/*
fun validateNewPassword(
    currentPassword: String?,
    newPassword: String?
): String? =
    if (currentPassword.isNullOrBlank() || newPassword.isNullOrBlank()) "Invalid empty password"
    else validatePassword(newPassword)

 */
fun validateEmail(field: String): String? =
    if (!Regex("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
        .matches(field)) "Invalid email format" else null
fun validatePhoneNumber(field: String?): String? =
    if (field.isNullOrBlank()) "Field required"
    else if (!Regex("^[0-9]{1,20}$").matches(field)) "Invalid phone number format"
    else null
fun validateRole(field: String): String? =
    if (Role.entries.firstOrNull { it.name == field } == null) "Invalid role" else null
fun validateRefreshToken(field: String) : String? =
    if (field.length != 32) "Invalid refresh token" else null
fun validateAcademicYearDates(startDate: kotlinx.datetime.LocalDate, endDate: kotlinx.datetime.LocalDate): String? =
    if (startDate > endDate) "Start date must be before end date" else null