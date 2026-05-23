package com.libreguardia.module.absence

import com.libreguardia.module.group.GroupEntity
import com.libreguardia.module.group.GroupTable
import com.libreguardia.module.place.PlaceEntity
import com.libreguardia.module.place.PlaceTable
import com.libreguardia.module.schedule_activity.ScheduleActivityEntity
import com.libreguardia.module.schedule_activity.ScheduleActivityTable
import com.libreguardia.module.service.ServiceEntity
import com.libreguardia.module.service.ServiceTable
import com.libreguardia.module.user.UserEntity
import com.libreguardia.module.user.UserTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time
import java.util.UUID

object AbsenceTable: UUIDTable(
    name = "absence"
) {
    val date = date(
        name = "date"
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
    val user = optReference(
        name = "user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_absence",
            date, startTime, endTime, user
        )
    }
}

class AbsenceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AbsenceEntity>(AbsenceTable)

    var date by AbsenceTable.date
    var startTime by AbsenceTable.startTime
    var endTime by AbsenceTable.endTime
    var group by GroupEntity.Companion optionalReferencedOn AbsenceTable.group
    var scheduleActivity by ScheduleActivityEntity.Companion referencedOn AbsenceTable.scheduleActivity
    var place by PlaceEntity.Companion referencedOn AbsenceTable.place
    var user by UserEntity.Companion optionalReferencedOn AbsenceTable.user
    val service by ServiceEntity.Companion optionalBackReferencedOn ServiceTable.absence
}