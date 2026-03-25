package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ServiceTbl: UUIDTable(
    name =  "service_tbl"
) {
    var is_signed = bool(
        name = "is_signed"
    ).default(false)
    var absenceId = reference(
        name = "absence_id",
        refColumn = AbsenceTbl.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    )
    var userId = optReference(
        name = "user_id",
        refColumn = UserTbl.id,
        onDelete = ReferenceOption.RESTRICT
    ).default(null)
}