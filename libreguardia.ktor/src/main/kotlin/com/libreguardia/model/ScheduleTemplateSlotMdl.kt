package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ScheduleTemplateSlotTbl: UUIDTable(
    name =  "schedule_template_slot"
) {
    var weekDay = enumerationByName<WeekDay>(
        name = "week_day",
        length = 9
    )
    var scheduleTemplateId = reference(
        name = "schedule_template_id",
        refColumn = ScheduleTemplateTbl.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    )
    var groupId = optReference(
        name = "group_id",
        refColumn = GroupTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    var scheduleActivityId = optReference(
        name = "schedule_activity_id",
        refColumn = ScheduleActivityTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    var placeId = optReference(
        "place_id",
        PlaceTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    var timeRangeId = reference(
        name = "time_range_id",
        refColumn = TimeRangeTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_schedule_template_slot",
            scheduleTemplateId, weekDay, timeRangeId
        )
    }
}