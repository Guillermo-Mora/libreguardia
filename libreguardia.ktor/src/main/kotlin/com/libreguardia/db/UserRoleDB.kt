package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object UserRoleTable: UUIDTable(
    name =  "user_role"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}