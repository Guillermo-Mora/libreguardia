package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.time

object TimeRangeTbl: UUIDTable(
    name =  "time_range"
) {
    val startTime = time(
        name = "start_time"
    )
    val endTime = time(
        name = "end_time"
    )

    init {
        uniqueIndex(
            customIndexName = "uq_time_range",
            startTime, endTime
        )
    }
}