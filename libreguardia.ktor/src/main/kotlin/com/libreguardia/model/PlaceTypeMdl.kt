package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object PlaceTypeTbl: UUIDTable(
    name = "place_type"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}