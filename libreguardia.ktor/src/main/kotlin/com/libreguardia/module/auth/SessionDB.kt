package com.libreguardia.module.auth

import com.libreguardia.module.user.UserEntity
import com.libreguardia.module.user.UserTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp
import java.util.UUID

object SessionTable: UUIDTable(
    name = "session"
) {
    val expiresAt = timestamp(
        name = "expires_at",
    )
    val user = reference(
        name = "user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT
    )
}

class SessionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SessionEntity>(SessionTable)
    var expiresAt by SessionTable.expiresAt
    var user by UserEntity.Companion referencedOn SessionTable.user
}