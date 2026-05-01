package com.libreguardia.repository

import com.libreguardia.db.model.SessionEntity
import com.libreguardia.db.model.SessionTable
import com.libreguardia.db.model.UserTable
import com.libreguardia.model.SessionModel
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.delete
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
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

    fun getUserSessionModel(
        uuid: UUID
    ): SessionModel? {
        return SessionTable
            .join(
                otherTable = UserTable,
                joinType = JoinType.INNER,
                onColumn = SessionTable.user,
                otherColumn = UserTable.id
            )
            .select(
                SessionTable.expiresAt,
                UserTable.isEnabled,
                UserTable.isDeleted,
                UserTable.id,
                UserTable.role
            )
            .where { SessionTable.id eq uuid }
            .limit(1)
            .map {
                SessionModel(
                    expiresAt = it[SessionTable.expiresAt],
                    isEnabled = it[UserTable.isEnabled],
                    isDeleted = it[UserTable.isDeleted],
                    userUuid = it[UserTable.id].value,
                    userRole = it[UserTable.role]
                )
            }
            .firstOrNull()
    }

    fun deleteSessionsFromUser(
        userUuid: UUID
    ) {
        SessionTable
            .deleteWhere { SessionTable.user eq userUuid }
    }

    fun deleteSession(
        uuid: UUID
    ) {
        SessionTable.deleteWhere { SessionTable.id eq uuid }
    }

    fun deleteOtherSessionsFromUser(sessionUuid: UUID, userUuid: UUID) {
        SessionTable.deleteWhere { SessionTable.user eq userUuid and (SessionTable.id neq sessionUuid) }

        /*
        SessionTable
            .join(
                otherTable = UserTable,
                joinType = JoinType.LEFT,
                onColumn = SessionTable.user,
                otherColumn = UserTable.id
            ).delete(SessionTable) {
                SessionTable.user eq userUuid and (SessionTable.id neq sessionUuid)
            }

         */
    }
}