package com.libreguardia.repository

import com.libreguardia.db.UserRoleTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import java.util.UUID

class UserRoleRepository {
    fun existsByUUID(
        uuid: UUID
    ): Boolean = UserRoleTable.select(UserRoleTable.id eq uuid).limit(1).any()
}