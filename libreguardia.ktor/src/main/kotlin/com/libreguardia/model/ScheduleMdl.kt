package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTbl: UUIDTable(
    name =  "schedule"
) {
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
    val weekDay = enumerationByName<WeekDay>(
        name = "week_day",
        length = 9
    )
    val groupId = optReference(
        name = "group_id",
        refColumn = GroupTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val scheduleActivityId = reference(
        name = "schedule_activity_id",
        refColumn = ScheduleActivityTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val placeId = reference(
        name = "place_id",
        refColumn = PlaceTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val timeRangeId = reference(
        name = "time_range_id",
        refColumn = TimeRangeTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val userId = reference(
        name = "user_id",
        refColumn = UserTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    val academicYearId = reference(
        name = "academic_year_id",
        refColumn = AcademicYearTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )

    init {
        uniqueIndex(
            customIndexName = "uq_schedule",
            weekDay, timeRangeId, userId, academicYearId
        )
    }
}