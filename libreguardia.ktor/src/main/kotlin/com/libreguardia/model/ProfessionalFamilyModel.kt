package com.libreguardia.model

import com.libreguardia.db.model.ProfessionalFamilyEntity
import java.util.UUID

data class ProfessionalFamilyModel(
    val id: String,
    val name: String
)

fun ProfessionalFamilyEntity.toModel() =
    ProfessionalFamilyModel(
        id = this.id.value.toString(),
        name = this.name
    )