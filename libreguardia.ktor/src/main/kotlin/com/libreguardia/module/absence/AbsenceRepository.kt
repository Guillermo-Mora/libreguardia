package com.libreguardia.module.absence

import com.libreguardia.module.shared.repository.BaseRepository
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.UUID

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