package com.libreguardia.frontend.component.main.edit

import com.libreguardia.dto.module.ProfessionalFamilyEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.FormFieldData
import com.libreguardia.frontend.component.OperationType
import com.libreguardia.frontend.component.customForm
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.*

enum class ProfessionalFamilyEditField(override val key: String) : FormField {
    NAME("name")
}

fun FlowContent.professionalFamilyEdit(
    dto: ProfessionalFamilyEditDTO,
    errors: Map<FormField, String?>? = null,
    uuid: UUID
) {
    customForm(
        formName = "edit-professional-family",
        previousPagePath = "/professional-family",
        operationType = OperationType.Patch,
        operationPath = "/professional-family/${uuid}",
        deletePath = "/professional-family/${uuid}",
        errors = errors,
        formFields = mapOf(
            ProfessionalFamilyEditField.NAME to FormFieldData(
                text = "name",
                value = dto.name,
                required = true,
                inputType = InputType.text
            ),
        ),
    )
}