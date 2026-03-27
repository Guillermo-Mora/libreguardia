package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object UserTable: UUIDTable(
    name =  "user_tbl"
) {
    val name = varchar(
        name = "name",
        length = 50
    )
    val surname = varchar(
        name = "surname",
        length = 50
    )
    val email = varchar(
        name = "email",
        length = 50
    ).uniqueIndex()
    val phoneNumber = varchar(
        name = "phone_number",
        length = 20
    )
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
    val password = varchar(
        name = "password",
        length = 60
    )
    val userRole = reference(
        name = "user_role_id",
        foreign = UserRoleTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
}