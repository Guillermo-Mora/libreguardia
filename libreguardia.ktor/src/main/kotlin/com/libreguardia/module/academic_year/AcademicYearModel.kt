package com.libreguardia.module.academic_year

import kotlinx.datetime.LocalDate
import java.util.UUID

data class AcademicYearModel(
    val id: UUID,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

fun AcademicYearEntity.toModel() =
    AcademicYearModel(
        id = this.id.value,
        name = this.name,
        startDate = this.startDate,
        endDate = this.endDate
    )