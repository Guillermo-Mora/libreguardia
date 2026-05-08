package com.libreguardia.repository

import com.libreguardia.db.model.AbsenceTable
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.*

class AbsenceRepository : BaseRepository<AbsenceTable>(AbsenceTable) {
    fun existsAbsenceByUserUUIDPreviousToNow(
        userUUID: UUID,
        dateTimeNow: LocalDateTime
    ): Boolean {
        return AbsenceTable
            .selectAll()
            .where {
                (AbsenceTable.user eq userUUID) and
                        (
                                (AbsenceTable.date less dateTimeNow.date) or
                                        (
                                                (AbsenceTable.date eq dateTimeNow.date) and
                                                        (AbsenceTable.endTime lessEq dateTimeNow.time)
                                                )

                                )
            }.limit(1).any()
    }

    fun deleteAbsencesByUserUUIDAfterNow(
        userUUID: UUID,
        dateTimeNow: LocalDateTime
    ) {
        AbsenceTable
            .deleteWhere {
                (AbsenceTable.user eq userUUID) and
                        (
                                (AbsenceTable.date greater dateTimeNow.date) or
                                        (
                                                (AbsenceTable.date eq dateTimeNow.date) and
                                                        (AbsenceTable.endTime greater dateTimeNow.time)
                                                )

                                )
            }
    }
}