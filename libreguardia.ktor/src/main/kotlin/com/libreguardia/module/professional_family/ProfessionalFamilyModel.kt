package com.libreguardia.module.professional_family

data class ProfessionalFamilyModel(
    val id: String,
    val name: String
)

fun ProfessionalFamilyEntity.toModel() =
    ProfessionalFamilyModel(
        id = this.id.value.toString(),
        name = this.name
    )