package com.libreguardia.dto.module

import com.libreguardia.db.model.ZoneEntity
import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.ZoneCreateField
import com.libreguardia.frontend.component.main.ZoneEditField
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.Parameters
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ZoneResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
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
    name = name
)

fun Parameters.toZoneCreateDTO() =
    ZoneCreateDTO(
        name = string(ZoneCreateField.NAME)
    )

fun Parameters.toZoneEditDTO() =
    ZoneEditDTO(
        name = string(ZoneEditField.NAME).takeIf { it.isNotBlank() }
    )

fun ZoneResponseDTO.toZoneEditDTO() =
    ZoneEditDTO(
        name = this.name
    )