package com.libreguardia.dto

import com.libreguardia.db.model.ZoneEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ZoneResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val isEnabled: Boolean
)

@Serializable
data class ZoneCreateDTO(
    val name: String
)

@Serializable
data class ZoneEditDTO(
    val name: String? = null
)

fun ZoneEntity.toResponseDTO() = ZoneResponseDTO(
    id = id.value,
    name = name,
    isEnabled = isEnabled
)