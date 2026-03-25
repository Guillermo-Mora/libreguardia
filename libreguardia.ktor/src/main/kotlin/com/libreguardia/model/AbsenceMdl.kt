package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.date

object AbsenceTbl: UUIDTable("absence") {
    var date = date("date")
    var scheduleId = reference("schedule_id", ScheduleTbl.id)

    init {
        uniqueIndex("uq_absence", date, scheduleId)
    }
}