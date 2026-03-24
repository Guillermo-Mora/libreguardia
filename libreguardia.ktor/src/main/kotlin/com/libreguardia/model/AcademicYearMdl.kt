package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.*

object AcademicYearTbl: UUIDTable("academic_year") {
    var name = varchar("name", 50).uniqueIndex()
    var startDate = date("start_date")
    var endDate = date("end_date")
}