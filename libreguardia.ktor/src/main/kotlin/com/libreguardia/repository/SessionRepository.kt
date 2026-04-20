package com.libreguardia.repository

import com.libreguardia.db.model.GroupTable
import com.libreguardia.db.model.ScheduleTable
import com.libreguardia.db.model.SessionEntity
import com.libreguardia.db.model.SessionTable
import com.libreguardia.db.model.UserEntity
import com.libreguardia.db.model.UserTable
import com.libreguardia.model.SessionModel
import com.libreguardia.model.dataToModel
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
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
}