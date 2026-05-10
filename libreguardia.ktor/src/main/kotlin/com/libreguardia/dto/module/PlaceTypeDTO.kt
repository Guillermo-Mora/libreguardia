package com.libreguardia.dto.module

import com.libreguardia.db.model.PlaceTypeEntity
import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.PlaceTypeCreateField
import com.libreguardia.frontend.component.main.edit.PlaceTypeEditField
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.Parameters
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

fun Parameters.toPlaceTypeCreateDTO() =
    PlaceTypeCreateDTO(
        name = string(PlaceTypeCreateField.NAME)
    )

fun Parameters.toPlaceTypeEditDTO() =
    PlaceTypeEditDTO(
        name = string(PlaceTypeEditField.NAME).takeIf { it.isNotBlank() }
    )

fun PlaceTypeResponseDTO.toPlaceTypeEditDTO() =
    PlaceTypeEditDTO(
        name = this.name
    )