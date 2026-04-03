package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.math.BigDecimal
import java.util.UUID

object ServiceTable: UUIDTable(
    name =  "service_tbl"
) {
    val pointsObtained = decimal(
        name = "points_obtained",
        precision = 8,
        scale = 1
    )
    val absence = reference(
        name = "absence_id",
        foreign = AbsenceTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    ).uniqueIndex()
    val coverUser = optReference(
        name = "cover_user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    ).default(null)
    val assignedUser = optReference(
        name = "assigned_user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
}

class ServiceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ServiceEntity>(ServiceTable)

    var pointsObtained by ServiceTable.pointsObtained
    var absence by AbsenceEntity referencedOn ServiceTable.absence
    var coverUser by UserEntity optionalReferencedOn ServiceTable.coverUser
    var assignedUser by UserEntity optionalReferencedOn ServiceTable.assignedUser
}