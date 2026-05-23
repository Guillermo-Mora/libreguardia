package com.libreguardia.module.place_type

import com.libreguardia.config.util.UUIDSerializer
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

/*
fun Parameters.toPlaceTypeCreateDTO() =
    PlaceTypeCreateDTO(
        name = string(PlaceTypeCreateField.NAME)
    )

fun Parameters.toPlaceTypeEditDTO() =
    PlaceTypeEditDTO(
        name = string(PlaceTypeEditField.NAME).takeIf { it.isNotBlank() }
    )

 */

fun PlaceTypeResponseDTO.toPlaceTypeEditDTO() =
    PlaceTypeEditDTO(
        name = this.name
    )