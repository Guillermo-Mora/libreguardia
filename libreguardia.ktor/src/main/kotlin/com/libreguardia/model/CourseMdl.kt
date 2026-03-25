package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object CourseTbl: UUIDTable("course") {
    var name = varchar("name", 50)
    var professionalFamilyId = reference("professional_family_id", ProfessionalFamilyTbl.id)
    var academicYearId = reference("academic_year_id", AcademicYearTbl.id)

    init {
        uniqueIndex("uq_course", name, academicYearId)
    }
}