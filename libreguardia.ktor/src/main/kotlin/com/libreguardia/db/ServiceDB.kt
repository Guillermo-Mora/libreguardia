package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import java.math.BigDecimal

object ServiceTable: UUIDTable(
    name =  "service_tbl"
) {
    val pointsObtained = decimal(
        name = "points_obtained",
        precision = 8,
        scale = 1
    ).default(BigDecimal.ONE)
    val absence = reference(
        name = "absence_id",
        foreign = AbsenceTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    )
    val coverUser = optReference(
        name = "cover_user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    ).default(null)
    val assignedUser = optReference(
        name = "assigned_user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.RESTRICT
    )
}