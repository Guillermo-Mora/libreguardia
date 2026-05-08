package com.libreguardia.dto.module

import com.libreguardia.db.model.AcademicYearEntity
import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.AcademicYearCreateField
import com.libreguardia.frontend.component.main.edit.AcademicYearEditField
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.Parameters
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
    endDate = endDate
)

fun Parameters.toAcademicYearCreateDTO() =
    AcademicYearCreateDTO(
        name = string(AcademicYearCreateField.NAME),
        startDate = LocalDate.parse(string(AcademicYearCreateField.START_DATE)),
        endDate = LocalDate.parse(string(AcademicYearCreateField.END_DATE))
    )

fun Parameters.toAcademicYearEditDTO() =
    AcademicYearEditDTO(
        name = string(AcademicYearEditField.NAME).takeIf { it.isNotBlank() },
        startDate = string(AcademicYearEditField.START_DATE).takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) },
        endDate = string(AcademicYearEditField.END_DATE).takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
    )
