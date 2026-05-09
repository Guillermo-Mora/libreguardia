package com.libreguardia.dto.module

import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.AcademicYearCreateField
import com.libreguardia.frontend.component.main.edit.AcademicYearEditField
import com.libreguardia.model.AcademicYearModel
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

data class AcademicYearEditDTO(
    val name: String,
    val startDate: String,
    val endDate: String
)

fun AcademicYearModel.toAcademicYearEditDTO() =
    AcademicYearEditDTO(
        name = this.name,
        startDate = this.startDate,
        endDate = this.endDate
    )


fun Parameters.toAcademicYearCreateDTO() =
    AcademicYearCreateDTO(
        name = string(AcademicYearCreateField.NAME),
        startDate = LocalDate.parse(string(AcademicYearCreateField.START_DATE)),
        endDate = LocalDate.parse(string(AcademicYearCreateField.END_DATE))
    )

fun Parameters.toAcademicYearEditDTO() =
    AcademicYearEditDTO(
        name = string(AcademicYearEditField.NAME),
        startDate = string(AcademicYearEditField.START_DATE),
        endDate = string(AcademicYearEditField.END_DATE)
    )
