package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleActivityTbl: UUIDTable("schedule_activity") {
    var name = varchar("name", 50).uniqueIndex()
    var generatesService = bool("generates_service")
}