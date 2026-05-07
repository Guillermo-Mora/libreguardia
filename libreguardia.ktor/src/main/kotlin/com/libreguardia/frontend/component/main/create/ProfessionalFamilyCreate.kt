package com.libreguardia.frontend.component.main.create

import com.libreguardia.dto.module.ProfessionalFamilyCreateDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.FormFieldData
import com.libreguardia.frontend.component.OperationType
import com.libreguardia.frontend.component.customForm
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class ProfessionalFamilyCreateField(override val key: String) : FormField {
    NAME("name")
}

fun FlowContent.professionalFamilyCreate(
    dto: ProfessionalFamilyCreateDTO = ProfessionalFamilyCreateDTO(),
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "edit-professional-family",
        previousPagePath = "/professional-family",
        operationType = OperationType.Post,
        operationPath = "/professional-family",
        errors = errors,
        formFields = mapOf(
            ProfessionalFamilyCreateField.NAME to FormFieldData(
                text = "name",
                value = dto.name,
                required = true,
                inputType = InputType.text
            ),
        ),
    )
}