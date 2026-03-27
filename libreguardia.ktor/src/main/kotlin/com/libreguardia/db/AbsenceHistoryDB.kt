package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

object AbsenceHistoryTable: UUIDTable(
    name = "absence_history"
) {
    val date = date(
        name = "date",
    )
    val userName = varchar(
        name = "user_name",
        length = 50
    )
    val userSurname = varchar(
        name = "user_surname",
        length = 50
    )
    val userEmail = varchar(
        name = "user_email",
        length = 50
    )
    val placeName = varchar(
        name = "place_name",
        length = 50
    )
    val scheduleActivityName = varchar(
        name = "schedule_activity_name",
        length = 50
    )
    val groupName = varchar(
        name = "group_name",
        length = 50
    ).nullable().default(null)
    val timeRangeStartTime = time(
        name = "time_range_start_time",
    )
    val timeRangeEndTime = time(
        name = "time_range_end_time",
    )
    val academicYear = reference(
        name = "academic_year_id",
        foreign = AcademicYearTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val absence = optReference(
        name = "absence_id",
        foreign = AbsenceTable,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.RESTRICT
    )
}