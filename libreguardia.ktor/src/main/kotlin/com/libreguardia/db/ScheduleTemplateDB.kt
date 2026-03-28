package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object ScheduleTemplateTable: UUIDTable(
    name =  "schedule_template"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}

class ScheduleTemplateEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ScheduleTemplateEntity>(ScheduleTemplateTable)

    var name by ScheduleTemplateTable.name
}