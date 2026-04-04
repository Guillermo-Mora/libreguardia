package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object ProfessionalFamilyTable: UUIDTable(
    name = "professional_family"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
}

class ProfessionalFamilyEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProfessionalFamilyEntity>(ProfessionalFamilyTable)

    var name by ProfessionalFamilyTable.name
    var isEnabled by ProfessionalFamilyTable.isEnabled
    val courses by CourseEntity referrersOn CourseTable.professionalFamily
}