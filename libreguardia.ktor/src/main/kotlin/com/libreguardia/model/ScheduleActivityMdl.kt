package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleActivityTbl: UUIDTable(
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