package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ServiceHistoryTable: UUIDTable(
    name = "service_history"
) {
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
    val absenceHistoryId = reference(
        name = "absence_history_id",
        refColumn = AbsenceHistoryTable.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val serviceId = optReference(
        name = "service_id",
        refColumn = ServiceTable.id,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.RESTRICT
    )
}