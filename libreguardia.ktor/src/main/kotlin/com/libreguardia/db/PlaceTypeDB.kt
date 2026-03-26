package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object PlaceTypeTable: UUIDTable(
    name = "place_type"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}