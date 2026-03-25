package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object BuildingTbl: UUIDTable(
    name = "building"
) {
    var name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}