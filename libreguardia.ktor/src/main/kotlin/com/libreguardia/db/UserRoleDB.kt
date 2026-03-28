package com.libreguardia.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object UserRoleTable: UUIDTable(
    name =  "user_role"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}

class UserRoleEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserRoleEntity>(UserRoleTable)

    var name by UserRoleTable.name
}