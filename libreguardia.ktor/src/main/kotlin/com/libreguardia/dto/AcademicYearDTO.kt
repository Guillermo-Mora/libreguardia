package com.libreguardia.dto

import com.libreguardia.db.model.AcademicYearEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AcademicYearResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isEnabled: Boolean = true
)

@Serializable
data class AcademicYearCreateDTO(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

@Serializable
data class AcademicYearEditDTO(
    val name: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

fun AcademicYearEntity.toResponseDTO() = AcademicYearResponseDTO(
    id = id.value,
    name = name,
    startDate = startDate,
    endDate = endDate,
    isEnabled = isEnabled
)
