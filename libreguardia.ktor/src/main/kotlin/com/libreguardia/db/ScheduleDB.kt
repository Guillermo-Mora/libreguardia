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
    val groupId = optReference(
        name = "group_id",
        refColumn = GroupTable.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val scheduleActivityId = reference(
        name = "schedule_activity_id",
        refColumn = ScheduleActivityTable.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val placeId = reference(
        name = "place_id",
        refColumn = PlaceTable.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val timeRangeId = reference(
        name = "time_range_id",
        refColumn = TimeRangeTable.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val userId = reference(
        name = "user_id",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_schedule",
            weekDay, timeRangeId, userId
        )
    }
}