package com.libreguardia.dto

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AcademicYearResponseDTO(
    val id: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

@Serializable
data class AcademicYearRequestDTO(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Name cannot be blank")
        }
        if (name.length > 50) {
            errors.add("Name cannot exceed 50 characters")
        }
        if (endDate.isBefore(startDate)) {
            errors.add("End date cannot be before start date")
        }
        return errors
    }
}
