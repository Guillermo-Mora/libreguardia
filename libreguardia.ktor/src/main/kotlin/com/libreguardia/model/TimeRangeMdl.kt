package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.time

object TimeRangeTbl: UUIDTable("time_range") {
    var name = varchar("name", 50).uniqueIndex()
    var startTime = time("start_time")
    var endTime = time("end_time")
}