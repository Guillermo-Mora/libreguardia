package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ServiceTable: UUIDTable(
    name =  "service_tbl"
) {
    val is_signed = bool(
        name = "is_signed"
    ).default(false)
    val absenceId = reference(
        name = "absence_id",
        refColumn = AbsenceTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    )
    val userId = optReference(
        name = "user_id",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.RESTRICT
    ).default(null)
}