package com.libreguardia.model

import com.libreguardia.db.model.CourseEntity
import java.util.*

data class CourseModel(
    val id: UUID,
    val name: String,
    val professionalFamilyId: UUID,
    val professionalFamilyName : String
)

fun CourseEntity.toModel() =
    CourseModel(
        id = this.id.value,
        name = this.name,
        professionalFamilyId = this.professionalFamily.id.value,
        professionalFamilyName = this.professionalFamily.name
    )