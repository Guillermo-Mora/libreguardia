package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ServiceHistoryTable: UUIDTable(
    name = "service_history"
) {
    val coverUserName = varchar(
        name = "cover_user_name",
        length = 50
    )
    val coverUserSurname = varchar(
        name = "cover_user_surname",
        length = 50
    )
    val coverUserEmail = varchar(
        name = "cover_user_email",
        length = 50
    )
    val assignedUserName = varchar(
        name = "assigned_user_name",
        length = 50
    ).nullable()
    val assignedUserSurname = varchar(
        name = "assigned_user_surname",
        length = 50
    ).nullable()
    val assignedUserEmail = varchar(
        name = "assigned_user_email",
        length = 50
    ).nullable()
    val absenceHistory = reference(
        name = "absence_history_id",
        foreign = AbsenceHistoryTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val service = optReference(
        name = "service_id",
        foreign = ServiceTable,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.RESTRICT
    )
    val coverUser = optReference(
        name = "cover_user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.RESTRICT
    )
    val assignedUser = optReference(
        name = "assigned_user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.RESTRICT
    )
}