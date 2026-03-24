package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object BuildingTbl: UUIDTable("building") {
    var name = varchar("name", 50).uniqueIndex()
}