package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTbl: UUIDTable("schedule") {
    var isEnabled = bool("is_enabled").default(true)
    var weekDay = enumerationByName<WeekDay>("week_day", 9)
    var groupId = optReference("group_id", GroupTbl.id)
    var scheduleActivityId = reference("schedule_activity_id", ScheduleActivityTbl.id)
    var placeId = reference("place_id", PlaceTbl.id)
    var timeRangeId = reference("time_range_id", TimeRangeTbl.id)
    var userId = reference("user_id", UserTbl.id)
    var academicYearId = reference("academic_year_id", AcademicYearTbl.id)

    init {
        uniqueIndex("uq_schedule", weekDay, timeRangeId, userId, academicYearId)
    }
}