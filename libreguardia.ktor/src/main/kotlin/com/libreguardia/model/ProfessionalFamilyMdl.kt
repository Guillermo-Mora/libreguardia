package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ProfessionalFamilyTbl: UUIDTable("professional_family") {
    var name = varchar("name", 50).uniqueIndex()
}