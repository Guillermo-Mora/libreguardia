package com.libreguardia.dto

import kotlinx.serialization.Serializable

@Serializable
data class CourseResponseDTO(
    val id: String,
    val name: String,
    val isEnabled: Boolean,
    val professionalFamilyId: String
)

@Serializable
data class CourseRequestDTO(
    val name: String,
    val isEnabled: Boolean,
    val professionalFamilyId: String
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Name cannot be blank")
        }
        if (name.length > 50) {
            errors.add("Name cannot exceed 50 characters")
        }
        if (professionalFamilyId.isBlank()) {
            errors.add("Professional family ID cannot be blank")
        }
        return errors
    }
}