package com.libreguardia.module.academic_year

import com.libreguardia.frontend.component.main.create.AcademicYearCreateField
import com.libreguardia.frontend.component.main.edit.AcademicYearEditField
import io.ktor.http.*
import kotlinx.datetime.LocalDate
import com.libreguardia.module.shared.validation.get

data class AcademicYearCreateDTO(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class AcademicYearEditDTO(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

fun AcademicYearModel.toAcademicYearEditDTO() =
    AcademicYearEditDTO(
        name = this.name,
        startDate = this.startDate,
        endDate = this.endDate
    )


fun Parameters.toAcademicYearCreateDTO() =
    AcademicYearCreateDTO(
        name = get<String>(AcademicYearCreateField.NAME),
        startDate = get<LocalDate>(AcademicYearCreateField.START_DATE),
        endDate = get<LocalDate>(AcademicYearCreateField.END_DATE)
    )

fun Parameters.toAcademicYearEditDTO() =
    AcademicYearEditDTO(
        name = get<String>(AcademicYearEditField.NAME),
        startDate = get<LocalDate>(AcademicYearEditField.START_DATE),
        endDate = get<LocalDate>(AcademicYearEditField.END_DATE)
    )
