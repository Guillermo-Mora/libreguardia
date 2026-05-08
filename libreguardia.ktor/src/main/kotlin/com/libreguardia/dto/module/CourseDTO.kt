package com.libreguardia.dto.module

import com.libreguardia.dto.string
import com.libreguardia.dto.uuid
import com.libreguardia.frontend.component.main.create.CourseCreateField
import com.libreguardia.frontend.component.main.edit.CourseEditField
import com.libreguardia.model.CourseModel
import io.ktor.http.*
import java.util.UUID

data class CourseEditDTO(
    val name: String,
    val professionalFamilyId: UUID
)

data class CourseCreateDTO(
    val name: String = "",
    val professionalFamilyId: UUID = UUID.randomUUID()
)

fun CourseModel.toCourseEditDTO() =
    CourseEditDTO(
        name = this.name,
        professionalFamilyId = this.professionalFamilyId
    )

fun Parameters.toCourseEditDTO() =
    CourseEditDTO(
        name = string(CourseEditField.NAME),
        professionalFamilyId = uuid(CourseEditField.PROFESSIONAL_FAMILY)
    )

fun Parameters.toCourseCreateDTO() =
    CourseCreateDTO(
        name = string(CourseCreateField.NAME),
        professionalFamilyId = uuid(CourseCreateField.PROFESSIONAL_FAMILY)
    )