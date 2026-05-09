package com.libreguardia.model

import com.libreguardia.db.model.CourseEntity
import java.util.*

data class CourseModel(
    val id: String,
    val name: String,
    val professionalFamilyId: String,
    val professionalFamilyName : String
)

fun CourseEntity.toModel() =
    CourseModel(
        id = this.id.value.toString(),
        name = this.name,
        professionalFamilyId = this.professionalFamily.id.value.toString(),
        professionalFamilyName = this.professionalFamily.name
    )