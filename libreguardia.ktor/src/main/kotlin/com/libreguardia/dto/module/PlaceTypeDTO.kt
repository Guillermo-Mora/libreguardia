package com.libreguardia.dto.module

import com.libreguardia.db.model.PlaceTypeEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlaceTypeResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
)

@Serializable
data class PlaceTypeCreateDTO(
    val name: String
)

@Serializable
data class PlaceTypeEditDTO(
    val name: String? = null
)

fun PlaceTypeEntity.toResponseDTO() = PlaceTypeResponseDTO(
    id = id.value,
    name = name
)