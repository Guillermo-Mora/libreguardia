package com.libreguardia.module.group

data class GroupModel(
    val id: String,
    val code: String,
    val pointsMultiplier: String,
    val courseId: String,
    val courseName: String,
    val academicYearId: String,
    val academicYearName: String
)

fun GroupEntity.toModel() =
    GroupModel(
        id = this.id.value.toString(),
        code = this.code,
        pointsMultiplier = this.pointsMultiplier.toString(),
        courseId = this.course.id.value.toString(),
        courseName = this.course.name,
        academicYearId = this.academicYear.id.value.toString(),
        academicYearName = this.academicYear.name
    )