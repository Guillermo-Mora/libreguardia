package com.libreguardia.dto.module

import com.libreguardia.db.model.BuildingEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BuildingResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
)

@Serializable
data class BuildingCreateDTO(
    val name: String
)

@Serializable
data class BuildingEditDTO(
    val name: String? = null
)

fun BuildingEntity.toResponseDTO() = BuildingResponseDTO(
    id = id.value,
    name = name,
)