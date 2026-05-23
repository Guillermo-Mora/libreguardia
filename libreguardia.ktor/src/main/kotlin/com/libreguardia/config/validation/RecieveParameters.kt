package com.libreguardia.config.validation

import com.libreguardia.config.database.Role
import com.libreguardia.frontend.component.FormField
import io.ktor.http.*
import io.ktor.server.plugins.*
import kotlinx.datetime.LocalDate
import java.math.BigDecimal
import java.util.UUID

//Helpers for receiving HTML forms parameters
inline fun <reified T> Parameters.get(field: FormField): T {
    val value = this[field.id]
    if (T::class == Boolean::class) { return (value == "checked") as T }
    if (value.isNullOrEmpty()) throw BadRequestException(message = "Required")
    return parseValue<T>(value)
}

inline fun <reified T> Parameters.getOrNull(field: FormField): T? {
    val value = this[field.id]
    if (T::class == Boolean::class) { return (value == "checked") as T }
    if (value.isNullOrEmpty()) return null
    return parseValue<T>(value)
}

inline fun <reified T> parseValue(value: String): T =
    runCatching {
        when (T::class) {
            String::class -> value
            Int::class -> value.toInt()
            BigDecimal::class -> value.toBigDecimal()
            Role::class -> Role.valueOf(value.uppercase())
            UUID::class -> UUID.fromString(value)
            LocalDate::class -> LocalDate.parse(value)
            else -> throw IllegalArgumentException()
        } as T
    }.getOrElse { throw BadRequestException("Invalid data type") }