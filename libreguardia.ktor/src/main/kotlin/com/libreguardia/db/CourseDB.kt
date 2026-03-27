package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

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