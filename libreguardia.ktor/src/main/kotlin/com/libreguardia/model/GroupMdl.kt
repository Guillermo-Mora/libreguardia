package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object GroupTbl: UUIDTable("group_tbl") {
    var name = varchar("name", 50)
    var courseId = reference("course_id", CourseTbl.id)

    init {
        uniqueIndex("uq_group", name, courseId)
    }
}