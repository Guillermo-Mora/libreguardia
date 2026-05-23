package com.libreguardia.module.service

import com.libreguardia.module.absence.AbsenceTable
import com.libreguardia.config.repository.BaseRepository
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ServiceRepository : BaseRepository<ServiceTable>(ServiceTable) {
    fun existsServiceAssignedOrCoveredByUserUUIDPreviousToNow(
        userUUID: UUID,
        dateTimeNow: LocalDateTime
    ): Boolean {
        return ServiceTable.join(
            AbsenceTable,
            JoinType.INNER,
            onColumn = ServiceTable.absence,
            otherColumn = AbsenceTable.id
        ).selectAll()
            .where {
                (ServiceTable.coverUser eq userUUID) or
                        (
                                (ServiceTable.assignedUser eq userUUID) and
                                        (
                                                (AbsenceTable.date less dateTimeNow.date) or
                                                        (
                                                                (AbsenceTable.date eq dateTimeNow.date) and
                                                                        (AbsenceTable.endTime lessEq dateTimeNow.time)
                                                                )

                                                )
                                )
            }.limit(1).any()
    }

    fun setNullAssignedServicesToUserUUIDAfterNow(
        userUUID: UUID,
        dateTimeNow: LocalDateTime
    ) {
        ServiceTable.join(
            AbsenceTable,
            JoinType.INNER,
            onColumn = ServiceTable.absence,
            otherColumn = AbsenceTable.id
        ).update({
            (ServiceTable.assignedUser eq userUUID) and
                    (
                            (AbsenceTable.date greater dateTimeNow.date) or
                                    (
                                            (AbsenceTable.date eq dateTimeNow.date) and
                                                    (AbsenceTable.endTime greater dateTimeNow.time)
                                            )

                            )
        }) {
            it[ServiceTable.assignedUser] = null
        }
    }
}