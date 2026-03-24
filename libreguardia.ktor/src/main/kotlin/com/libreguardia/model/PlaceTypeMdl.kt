package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object PlaceTypeTbl: UUIDTable("place_type") {
    var name = varchar("name", 50).uniqueIndex()
}