package com.libreguardia.db.model

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

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

class ScheduleActivityEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ScheduleActivityEntity>(ScheduleActivityTable)

    var name by ScheduleActivityTable.name
    var generatesService by ScheduleActivityTable.generatesService
}