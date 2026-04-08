package com.libreguardia.repository

import com.libreguardia.db.RefreshTokenTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import java.util.UUID

class RefreshTokenRepository {
    fun deleteRefreshTokensByUser(
        userUUID: UUID
    ) {
        RefreshTokenTable.deleteWhere { RefreshTokenTable.user eq userUUID }
    }
}