package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import org.jetbrains.exposed.v1.datetime.time
import java.util.UUID

object ScheduleTable: UUIDTable(
    name =  "schedule"
) {
    val weekDay = enumerationByName<WeekDay>(
        name = "week_day",
        length = 9
    )
    val startTime = time(
        name = "start_time"
    )
    val endTime = time(
        name = "end_time"
    )
    val group = optReference(
        name = "group_id",
        foreign = GroupTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val scheduleActivity = reference(
        name = "schedule_activity_id",
        foreign = ScheduleActivityTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val place = reference(
        name = "place_id",
        foreign = PlaceTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val user = reference(
        name = "user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_schedule",
            weekDay, startTime, endTime, user
        )
    }
}

class ScheduleEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ScheduleEntity>(ScheduleTable)

    var weekDay by ScheduleTable.weekDay
    var startTime by ScheduleTable.startTime
    var endTime by ScheduleTable.endTime
    var group by GroupEntity optionalReferencedOn ScheduleTable.group
    var scheduleActivity by ScheduleActivityEntity referencedOn ScheduleTable.scheduleActivity
    var place by PlaceEntity referencedOn ScheduleTable.place
    var user by UserEntity referencedOn ScheduleTable.user
}