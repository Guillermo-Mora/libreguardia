package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTemplateTable: UUIDTable(
    name =  "schedule_template"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}