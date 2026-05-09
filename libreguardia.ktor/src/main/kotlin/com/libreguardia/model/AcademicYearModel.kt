package com.libreguardia.model

import com.libreguardia.db.model.AcademicYearEntity

data class AcademicYearModel(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String
)

fun AcademicYearEntity.toModel() =
    AcademicYearModel(
        id = this.id.value.toString(),
        name = this.name,
        startDate = this.startDate.toString(),
        endDate = this.endDate.toString()
    )