package com.libreguardia.db

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
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
}

class ScheduleActivityEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ScheduleActivityEntity>(ScheduleActivityTable)

    var name by ScheduleActivityTable.name
    var generatesService by ScheduleActivityTable.generatesService
    var isEnabled by ScheduleActivityTable.isEnabled
}