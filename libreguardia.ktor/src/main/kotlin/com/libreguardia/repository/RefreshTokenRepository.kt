package com.libreguardia.repository

import com.libreguardia.db.model.RefreshTokenEntity
import com.libreguardia.db.model.RefreshTokenTable
import com.libreguardia.db.model.UserTable
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import java.util.*
import kotlin.time.Instant

class RefreshTokenRepository {
    fun deleteRefreshTokensByUser(
        userUUID: UUID
    ) {
        RefreshTokenTable.deleteWhere { RefreshTokenTable.user eq userUUID }
    }

    fun save(
        newRefreshTokenPrefix: String,
        newRefreshTokenHash: String,
        expiration: Instant,
        userUuid: UUID
    ) {
        RefreshTokenTable.insert {
            it[refreshTokenPrefix] = newRefreshTokenPrefix
            it[refreshTokenHash] = newRefreshTokenHash
            it[expiresAt] = expiration
            it[user] = userUuid
        }
    }

    fun getRefreshTokenEntity(
        refreshTokenPrefix: String,
    ): RefreshTokenEntity? =
        RefreshTokenEntity.find { RefreshTokenTable.refreshTokenPrefix eq refreshTokenPrefix }
            .limit(1).firstOrNull()

    fun getRefreshTokenHash(
        refreshTokenPrefix: String,
        userUuid: UUID
    ): String? = RefreshTokenTable
        .select(RefreshTokenTable.refreshTokenHash)
        .where {
            RefreshTokenTable.refreshTokenPrefix eq refreshTokenPrefix and
                    (RefreshTokenTable.id eq userUuid)
        }
        .limit(1)
        .map { it[RefreshTokenTable.refreshTokenHash] }
        .firstOrNull()

    fun deleteRefreshToken(
        uuid: UUID
    ): Boolean {
        return RefreshTokenTable.deleteWhere { RefreshTokenTable.id eq uuid } == 1
    }

    fun deleteRefreshToken(
        refreshTokenPrefix: String,
        userUUID: UUID
    ): Boolean =
        RefreshTokenTable.deleteWhere {
            RefreshTokenTable.refreshTokenPrefix eq refreshTokenPrefix and
                    (RefreshTokenTable.user eq userUUID)
        } == 1
}