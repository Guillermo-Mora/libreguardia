package com.libreguardia.dto.module

import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.GroupCreateField
import com.libreguardia.frontend.component.main.edit.GroupEditField
import com.libreguardia.model.GroupModel
import io.ktor.http.*
import java.util.*

data class GroupEditDTO(
    val code: String,
    val pointsMultiplier: String,
    val courseId: String,
    val academicYearId: String
)

data class GroupCreateDTO(
    val code: String = "",
    val pointsMultiplier: String = "0",
    val courseId: String = "",
    val academicYearId: String = ""
)

fun GroupModel.toGroupEditDTO() =
    GroupEditDTO(
        code = this.code,
        pointsMultiplier = this.pointsMultiplier,
        courseId = this.courseId,
        academicYearId = this.academicYearId
    )

fun Parameters.toGroupEditDTO() =
    GroupEditDTO(
        code = string(GroupEditField.CODE),
        pointsMultiplier = string(GroupEditField.POINTS_MULTIPLIER),
        courseId = string(GroupEditField.COURSE),
        academicYearId = string(GroupEditField.ACADEMIC_YEAR)
    )

fun Parameters.toGroupCreateDTO() =
    GroupCreateDTO(
        code = string(GroupCreateField.CODE),
        pointsMultiplier = string(GroupCreateField.POINTS_MULTIPLIER),
        courseId = string(GroupCreateField.COURSE),
        academicYearId = string(GroupCreateField.ACADEMIC_YEAR)
    )