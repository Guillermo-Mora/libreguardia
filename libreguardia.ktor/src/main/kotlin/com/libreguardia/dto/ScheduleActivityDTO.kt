package com.libreguardia.dto

import com.libreguardia.db.model.ScheduleActivityEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ScheduleActivityResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val generatesService: Boolean,
    val isEnabled: Boolean = true
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
    generatesService = generatesService,
    isEnabled = isEnabled
)

