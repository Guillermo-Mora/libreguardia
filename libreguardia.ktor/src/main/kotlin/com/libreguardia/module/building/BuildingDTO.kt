package com.libreguardia.module.building

import com.libreguardia.module.shared.validation.get
import com.libreguardia.frontend.component.main.create.BuildingCreateField
import com.libreguardia.frontend.component.main.edit.BuildingEditField
import io.ktor.http.*
import java.util.*

data class BuildingResponseDTO(
    val id: UUID,
    val name: String,
)

data class BuildingCreateDTO(
    val name: String
)

data class BuildingEditDTO(
    val name: String
)

fun BuildingEntity.toResponseDTO() = BuildingResponseDTO(
    id = id.value,
    name = name,
)

fun Parameters.toBuildingCreateDTO() =
    BuildingCreateDTO(
        name = get<String>(BuildingCreateField.NAME)
    )

fun Parameters.toBuildingEditDTO() =
    BuildingEditDTO(
        name = get<String>(BuildingEditField.NAME)
    )

fun BuildingResponseDTO.toBuildingEditDTO() =
    BuildingEditDTO(
        name = this.name
    )