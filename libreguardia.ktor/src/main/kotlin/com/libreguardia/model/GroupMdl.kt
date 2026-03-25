package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object GroupTbl: UUIDTable(
    name = "group_tbl"
) {
    var name = varchar(
        name = "name",
        length = 50
    )
    var courseId = reference(
        name = "course_id",
        refColumn = CourseTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )

    init {
        uniqueIndex(
            customIndexName = "uq_group",
            name, courseId
        )
    }
}