package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleActivityTable: UUIDTable(
     name = "schedule_activity"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    val generatesService = bool(
        name = "generates_service"
    )
}