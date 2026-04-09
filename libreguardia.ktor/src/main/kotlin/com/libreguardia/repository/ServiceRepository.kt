package com.libreguardia.repository

import com.libreguardia.db.model.AbsenceTable
import com.libreguardia.db.model.ServiceTable
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.*

class ServiceRepository {
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