package com.libreguardia.model

import com.libreguardia.db.model.GroupEntity
import java.math.BigDecimal
import java.util.*

data class GroupModel(
    val id: UUID,
    val code: String,
    val pointsMultiplier: String,
    val courseId: UUID,
    val courseName: String
)

fun GroupEntity.toModel() =
    GroupModel(
        id = this.id.value,
        code = this.code,
        pointsMultiplier = this.pointsMultiplier.toString(),
        courseId = this.course.id.value,
        courseName = this.course.name
    )