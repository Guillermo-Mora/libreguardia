package com.libreguardia.db.model

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import org.jetbrains.exposed.v1.datetime.*
import java.util.UUID

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
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
}

class AcademicYearEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AcademicYearEntity>(AcademicYearTable)

    var name by AcademicYearTable.name
    var startDate by AcademicYearTable.startDate
    var endDate by AcademicYearTable.endDate
    var isEnabled by AcademicYearTable.isEnabled
}