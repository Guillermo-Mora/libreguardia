package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTemplateSlotTbl: UUIDTable("schedule_template_slot") {
    var weekDay = enumerationByName<WeekDay>("week_day", 9)
    var scheduleTemplateId = reference("schedule_template_id", ScheduleTemplateTbl.id)
    var groupId = optReference("group_id", GroupTbl.id)
    var scheduleActivityId = optReference("schedule_activity_id", ScheduleActivityTbl.id)
    var placeId = optReference("place_id", PlaceTbl.id)
    var timeRangeId = reference("time_range_id", TimeRangeTbl.id)

    init {
        uniqueIndex("uq_schedule_template_slot", scheduleTemplateId, weekDay, timeRangeId)
    }
}