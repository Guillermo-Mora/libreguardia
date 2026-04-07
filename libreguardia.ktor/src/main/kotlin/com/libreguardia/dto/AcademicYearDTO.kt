package com.libreguardia.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class AcademicYearResponseDTO(
    val id: String,
    val name: String,
    @Contextual val startDate: LocalDate,
    @Contextual val endDate: LocalDate
)

@Serializable
data class AcademicYearRequestDTO(
    val name: String,
    @Contextual val startDate: LocalDate,
    @Contextual val endDate: LocalDate
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Name cannot be blank")
        }
        if (name.length > 50) {
            errors.add("Name cannot exceed 50 characters")
        }
        if (endDate < startDate) {
            errors.add("End date cannot be before start date")
        }
        return errors
    }
}
