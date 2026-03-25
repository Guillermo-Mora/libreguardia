package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.date

object AbsenceTbl: UUIDTable(
    name = "absence"
) {
    val date = date(
        name = "date"
    )
    val scheduleId = reference(
        name = "schedule_id",
        refColumn = ScheduleTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )

    init {
        uniqueIndex(
            customIndexName = "uq_absence",
            date, scheduleId
        )
    }
}