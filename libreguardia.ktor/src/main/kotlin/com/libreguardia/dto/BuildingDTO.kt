package com.libreguardia.dto

import com.libreguardia.db.model.BuildingEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BuildingResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val isEnabled: Boolean = true
)

@Serializable
data class BuildingCreateDTO(
    val name: String
)

@Serializable
data class BuildingEditDTO(
    val name: String? = null,
    val isEnabled: Boolean? = null
)

fun BuildingEntity.toResponseDTO() = BuildingResponseDTO(
    id = id.value,
    name = name,
    isEnabled = isEnabled
)