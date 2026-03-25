package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object UserTbl: UUIDTable(
    name =  "user_tbl"
) {
    var name = varchar(
        name = "name",
        length = 50
    )
    var surname = varchar(
        name = "surname",
        length = 50
    )
    var email = varchar(
        name = "email",
        length = 50
    ).uniqueIndex()
    var phoneNumber = varchar(
        name = "phone_number",
        length = 20
    )
    var isEnabled = bool(
        name = "is_enabled"
    ).default(true)
    var password = varchar(
        name = "password",
        length = 60
    )
    var userRoleId = reference(
        name = "user_role_id",
        refColumn = UserRoleTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )
}