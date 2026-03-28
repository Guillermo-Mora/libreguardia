package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.math.BigDecimal
import java.util.UUID

object GroupTable: UUIDTable(
    name = "group_tbl"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    val pointsMultiplier = decimal(
        name = "points_multiplier",
        precision = 2,
        scale = 1
    ).default(BigDecimal.ONE)
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
    val course = reference(
        name = "course_id",
        foreign = CourseTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
}

class GroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<GroupEntity>(GroupTable)

    var name by GroupTable.name
    var pointsMultiplier by GroupTable.pointsMultiplier
    var isEnabled by GroupTable.isEnabled
    var course by CourseEntity referencedOn GroupTable.course
}