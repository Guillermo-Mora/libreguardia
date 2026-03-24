package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTemplateTbl: UUIDTable("schedule_template") {
    var name = varchar("name", 50).uniqueIndex()
}