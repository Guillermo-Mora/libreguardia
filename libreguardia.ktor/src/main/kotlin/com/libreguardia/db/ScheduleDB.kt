package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTable: UUIDTable(
    name =  "schedule"
) {
    val weekDay = enumerationByName<WeekDay>(
        name = "week_day",
        length = 9
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
    val timeRange = reference(
        name = "time_range_id",
        foreign = TimeRangeTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val user = reference(
        name = "user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_schedule",
            weekDay, timeRange, user
        )
    }
}