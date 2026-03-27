package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.date

object AbsenceTable: UUIDTable(
    name = "absence"
) {
    val date = date(
        name = "date"
    )
    val schedule = reference(
        name = "schedule_id",
        foreign = ScheduleTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    )

    init {
        uniqueIndex(
            customIndexName = "uq_absence",
            date, schedule
        )
    }
}