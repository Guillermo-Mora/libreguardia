package com.libreguardia.db.model

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
    val refreshTokenPrefix = varchar(
        name = "refresh_token_prefix",
        length = 60
    )
    val refreshTokenHash = varchar(
        name = "refresh_token_hash",
        length = 60
    )
    val expiresAt = timestamp(
        name = "expires_at"
    )
    val user = reference(
        name = "user_id",
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_absence",
            refreshTokenPrefix, refreshTokenHash, user
        )
    }
}

class RefreshTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshTokenEntity>(RefreshTokenTable)

    var refreshTokenPrefix by RefreshTokenTable.refreshTokenPrefix
    var refreshTokenHash by RefreshTokenTable.refreshTokenHash
    var expiresAt by RefreshTokenTable.expiresAt
    var user by UserEntity referencedOn RefreshTokenTable.user
}
