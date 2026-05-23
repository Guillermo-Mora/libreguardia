package com.libreguardia.module.professional_family

data class ProfessionalFamilyEditDTO(
    val name: String
)

data class ProfessionalFamilyCreateDTO(
    val name: String = ""
)

fun ProfessionalFamilyModel.toProfessionalFamilyEditDTO() =
    ProfessionalFamilyEditDTO(
        name = this.name,
    )

/*
fun Parameters.toProfessionalFamilyEditDTO() =
    ProfessionalFamilyEditDTO(
        name = string(ProfessionalFamilyEditField.NAME)
    )

fun Parameters.toProfessionalFamilyCreateDTO() =
    ProfessionalFamilyCreateDTO(
        name = string(ProfessionalFamilyCreateField.NAME)
    )

 */