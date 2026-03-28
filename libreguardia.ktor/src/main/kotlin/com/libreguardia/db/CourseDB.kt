package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object CourseTable: UUIDTable(
    name = "course"
) {
    val name = varchar(
        name = "name",
        length = 50,
    ).uniqueIndex()
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
    val professionalFamily = reference(
        name = "professional_family_id",
        foreign = ProfessionalFamilyTable
    )
}

class CourseEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CourseEntity>(CourseTable)

    var name by CourseTable.name
    var isEnabled by CourseTable.isEnabled
    var professionalFamily by ProfessionalFamilyEntity referencedOn CourseTable.professionalFamily
}