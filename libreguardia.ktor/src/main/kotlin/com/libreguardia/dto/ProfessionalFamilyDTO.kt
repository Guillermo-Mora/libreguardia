package com.libreguardia.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfessionalFamilyResponseDTO(
    val id: String,
    val name: String,
    val isEnabled: Boolean
)

@Serializable
data class ProfessionalFamilyRequestDTO(
    val name: String,
    val isEnabled: Boolean
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Name cannot be blank")
        }
        if (name.length > 50) {
            errors.add("Name cannot exceed 50 characters")
        }
        return errors
    }
}
