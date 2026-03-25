package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object UserRoleTbl: UUIDTable(
    name =  "user_role"
) {
    var name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}