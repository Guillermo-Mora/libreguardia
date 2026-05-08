package com.libreguardia.dto.module

import com.libreguardia.db.model.ScheduleActivityEntity
import com.libreguardia.dto.boolean
import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.ScheduleActivityCreateField
import com.libreguardia.frontend.component.main.edit.ScheduleActivityEditField
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.Parameters
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ScheduleActivityResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val generatesService: Boolean,
)

@Serializable
data class ScheduleActivityCreateDTO(
    val name: String,
    val generatesService: Boolean
)

@Serializable
data class ScheduleActivityEditDTO(
    val name: String? = null,
    val generatesService: Boolean? = null
)

fun ScheduleActivityEntity.toResponseDTO() = ScheduleActivityResponseDTO(
    id = id.value,
    name = name,
    generatesService = generatesService
)

fun Parameters.toScheduleActivityCreateDTO() =
    ScheduleActivityCreateDTO(
        name = string(ScheduleActivityCreateField.NAME),
        generatesService = boolean(ScheduleActivityCreateField.GENERATES_SERVICE)
    )

fun Parameters.toScheduleActivityEditDTO() =
    ScheduleActivityEditDTO(
        name = string(ScheduleActivityEditField.NAME).takeIf { it.isNotBlank() },
        generatesService = boolean(ScheduleActivityEditField.GENERATES_SERVICE)
    )

fun ScheduleActivityResponseDTO.toScheduleActivityEditDTO() =
    ScheduleActivityEditDTO(
        name = this.name,
        generatesService = this.generatesService
    )

