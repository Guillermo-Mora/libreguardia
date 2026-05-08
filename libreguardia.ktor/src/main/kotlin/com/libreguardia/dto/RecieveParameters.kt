package com.libreguardia.dto

import com.libreguardia.frontend.component.FormField
import io.ktor.http.Parameters
import java.math.BigDecimal
import java.util.UUID
import kotlin.text.uppercase

//Helpers for receiving HTML forms parameters
fun Parameters.string(field: FormField) = this[field.id] ?: ""
fun Parameters.boolean(field: FormField) = this[field.id].let { it == "checked" }
fun Parameters.enum(field: FormField) = this[field.id]?.uppercase() ?: ""