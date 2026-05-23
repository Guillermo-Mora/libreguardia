package com.libreguardia.module.schedule_activity

import com.libreguardia.config.util.UUIDSerializer
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

/*
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

 */

fun ScheduleActivityResponseDTO.toScheduleActivityEditDTO() =
    ScheduleActivityEditDTO(
        name = this.name,
        generatesService = this.generatesService
    )

