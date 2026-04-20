package com.libreguardia.dto

import com.libreguardia.db.model.ProfessionalFamilyEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProfessionalFamilyResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val isEnabled: Boolean = true
)

@Serializable
data class ProfessionalFamilyCreateDTO(
    val name: String
)

@Serializable
data class ProfessionalFamilyEditDTO(
    val name: String? = null
)

fun ProfessionalFamilyEntity.toResponseDTO() = ProfessionalFamilyResponseDTO(
    id = id.value,
    name = name,
    isEnabled = isEnabled
)