package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.*

object AcademicYearTbl: UUIDTable(
    name = "academic_year"
) {
    var name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    var startDate = date(
        name = "start_date"
    )
    var endDate = date(
        name = "end_date"
    )
}