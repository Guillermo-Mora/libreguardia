package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object CourseTbl: UUIDTable(
    name = "course"
) {
    val name = varchar(
        name = "name",
        length = 50
    )
    val professionalFamilyId = reference(
        name = "professional_family_id",
        refColumn = ProfessionalFamilyTbl.id
    )
    val academicYearId = reference(
        name = "academic_year_id",
        refColumn = AcademicYearTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )

    init {
        uniqueIndex(
            customIndexName = "uq_course",
            name, academicYearId
        )
    }
}