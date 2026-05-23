package com.libreguardia.module.course

import com.libreguardia.module.group.GroupEntity
import com.libreguardia.module.group.GroupTable
import com.libreguardia.module.professional_family.ProfessionalFamilyEntity
import com.libreguardia.module.professional_family.ProfessionalFamilyTable
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
    val professionalFamily = reference(
        name = "professional_family_id",
        foreign = ProfessionalFamilyTable
    )
}

class CourseEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CourseEntity>(CourseTable)

    var name by CourseTable.name
    var professionalFamily by ProfessionalFamilyEntity.Companion referencedOn CourseTable.professionalFamily
    val groups by GroupEntity.Companion referrersOn GroupTable.course
}