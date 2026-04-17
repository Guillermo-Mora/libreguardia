package com.libreguardia.repository

import com.libreguardia.db.model.SessionEntity
import com.libreguardia.db.model.SessionTable
import com.libreguardia.db.model.UserEntity
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import java.util.UUID
import kotlin.time.Instant

class SessionRepository {
    fun save(
        uuid: UUID,
        expiration: Instant,
        userUuid: UUID
    ) {
        SessionTable.insert {
            it[id] = uuid
            it[expiresAt] = expiration
            it[user] = userUuid
        }
    }

    fun getSessionEntityWithUserLoaded(
        uuid: UUID
    ): SessionEntity? {
        return SessionEntity
            .findById(uuid)
            ?.load(SessionEntity::user)
    }

    fun deleteSessionsFromUser(
        userUuid: UUID
    ) {
        SessionTable
            .deleteWhere { SessionTable.user eq userUuid }
    }
}