package com.libreguardia.module.course

data class CourseEditDTO(
    val name: String,
    val professionalFamilyId: String
)

data class CourseCreateDTO(
    val name: String = "",
    val professionalFamilyId: String = ""
)

fun CourseModel.toCourseEditDTO() =
    CourseEditDTO(
        name = this.name,
        professionalFamilyId = this.professionalFamilyId
    )

/*
fun Parameters.toCourseEditDTO() =
    CourseEditDTO(
        name = string(CourseEditField.NAME),
        professionalFamilyId = string(CourseEditField.PROFESSIONAL_FAMILY)
    )

fun Parameters.toCourseCreateDTO() =
    CourseCreateDTO(
        name = string(CourseCreateField.NAME),
        professionalFamilyId = string(CourseCreateField.PROFESSIONAL_FAMILY)
    )

 */