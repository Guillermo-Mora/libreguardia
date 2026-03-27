package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object GroupTable: UUIDTable(
    name = "group_tbl"
) {
    val name = varchar(
        name = "name",
        length = 50
    )
    val course = reference(
        name = "course_id",
        foreign = CourseTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )

    init {
        uniqueIndex(
            customIndexName = "uq_group",
            name, course
        )
    }
}