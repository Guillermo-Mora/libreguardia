package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp
import java.util.*

object RefreshTokenTable: UUIDTable(
    name = "refresh_token"
) {
    val refreshToken = varchar(
        name = "refresh_token",
        length = 60
    )
    val expiresAt = timestamp(
        name = "expires_at"
    )
    val revoked = bool(
        name = "revoked"
    ).default(false)
    val user = reference(
        name = "user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_absence",
            refreshToken, user
        )
    }
}

class RefreshTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshTokenEntity>(RefreshTokenTable)

    var refreshToken by RefreshTokenTable.refreshToken
    var expiresAt by RefreshTokenTable.expiresAt
    var revoked by RefreshTokenTable.revoked
    var user by UserEntity referencedOn RefreshTokenTable.user
}