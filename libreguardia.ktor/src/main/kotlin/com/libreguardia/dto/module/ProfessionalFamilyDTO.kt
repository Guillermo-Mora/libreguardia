package com.libreguardia.dto.module

import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.ProfessionalFamilyCreateField
import com.libreguardia.frontend.component.main.edit.ProfessionalFamilyEditField
import com.libreguardia.model.ProfessionalFamilyModel
import io.ktor.http.*

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

fun Parameters.toProfessionalFamilyEditDTO() =
    ProfessionalFamilyEditDTO(
        name = string(ProfessionalFamilyEditField.NAME)
    )

fun Parameters.toProfessionalFamilyCreateDTO() =
    ProfessionalFamilyCreateDTO(
        name = string(ProfessionalFamilyCreateField.NAME)
    )