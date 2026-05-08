package com.libreguardia.dto.module

import com.libreguardia.db.model.BuildingEntity
import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.BuildingCreateField
import com.libreguardia.frontend.component.main.edit.BuildingEditField
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.Parameters
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

fun Parameters.toBuildingCreateDTO() =
    BuildingCreateDTO(
        name = string(BuildingCreateField.NAME)
    )

fun Parameters.toBuildingEditDTO() =
    BuildingEditDTO(
        name = string(BuildingEditField.NAME).takeIf { it.isNotBlank() }
    )

fun BuildingResponseDTO.toBuildingEditDTO() =
    BuildingEditDTO(
        name = this.name
    )