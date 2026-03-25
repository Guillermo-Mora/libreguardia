package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTbl: UUIDTable(
    name =  "schedule"
) {
    var isEnabled = bool(
        name = "is_enabled"
    ).default(true)
    var weekDay = enumerationByName<WeekDay>(
        name = "week_day",
        length = 9
    )
    var groupId = optReference(
        name = "group_id",
        refColumn = GroupTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    var scheduleActivityId = reference(
        name = "schedule_activity_id",
        refColumn = ScheduleActivityTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    var placeId = reference(
        name = "place_id",
        refColumn = PlaceTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    var timeRangeId = reference(
        name = "time_range_id",
        refColumn = TimeRangeTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    var userId = reference(
        name = "user_id",
        refColumn = UserTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
    var academicYearId = reference(
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