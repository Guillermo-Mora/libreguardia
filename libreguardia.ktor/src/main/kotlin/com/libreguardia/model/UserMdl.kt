package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object UserTbl: UUIDTable("user_tbl") {
    var name = varchar("name", 50)
    var surname = varchar("surname", 50)
    var email = varchar("email", 50).uniqueIndex()
    var phoneNumber = varchar("phone_number", 20)
    var isEnabled = bool("is_enabled").default(true)
    var password = varchar("password", 60)
    var userRoleId = reference("user_role_id", UserRoleTbl.id)
}