package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ProfessionalFamilyTbl: UUIDTable(
    name = "professional_family"
) {
    var name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}