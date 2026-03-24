package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object UserRoleTbl: UUIDTable("user_role") {
    var name = varchar("name", 50).uniqueIndex()
}