package com.libreguardia.repository

import com.libreguardia.db.model.ScheduleTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import java.util.*

class ScheduleRepository {
    fun deleteSchedulesByUserUUID(
        userUUID: UUID
    ) {
        ScheduleTable.deleteWhere { ScheduleTable.user eq userUUID }
    }
}