package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import org.jetbrains.exposed.v1.datetime.time
import java.util.UUID

object ScheduleTemplateSlotTable: UUIDTable(
    name =  "schedule_template_slot"
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
    val scheduleTemplate = reference(
        name = "schedule_template_id",
        foreign = ScheduleTemplateTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    )
    val group = optReference(
        name = "group_id",
        foreign = GroupTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val scheduleActivity = optReference(
        name = "schedule_activity_id",
        foreign = ScheduleActivityTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val place = optReference(
        name = "place_id",
        foreign =  PlaceTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )

    init {
        uniqueIndex(
            customIndexName = "uq_schedule_template_slot",
            weekDay, startTime, endTime, scheduleTemplate
        )
    }
}

class ScheduleTemplateSlotEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ScheduleTemplateSlotEntity>(ScheduleTemplateSlotTable)

    var weekDay by ScheduleTemplateSlotTable.weekDay
    var startTime by ScheduleTemplateSlotTable.startTime
    var endTime by ScheduleTemplateSlotTable.endTime
    var scheduleTemplate by ScheduleTemplateEntity referencedOn ScheduleTemplateSlotTable.scheduleTemplate
    var group by GroupEntity optionalReferencedOn ScheduleTemplateSlotTable.group
    var scheduleActivity by ScheduleActivityEntity optionalReferencedOn ScheduleTemplateSlotTable.scheduleActivity
    var place by PlaceEntity optionalReferencedOn ScheduleTemplateSlotTable.place
}