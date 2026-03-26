package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ProfessionalFamilyTable: UUIDTable(
    name = "professional_family"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}