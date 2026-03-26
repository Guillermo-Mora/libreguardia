package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.*

object AcademicYearTable: UUIDTable(
    name = "academic_year"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    val startDate = date(
        name = "start_date"
    )
    val endDate = date(
        name = "end_date"
    )
}