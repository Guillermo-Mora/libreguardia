package com.libreguardia.frontend.component.main.create

import com.libreguardia.module.professional_family.ProfessionalFamilyCreateDTO
import com.libreguardia.frontend.component.FormField
import kotlinx.html.FlowContent

enum class ProfessionalFamilyCreateField(override val key: String) : FormField {
    NAME("name")
}

fun FlowContent.professionalFamilyCreate(
    dto: ProfessionalFamilyCreateDTO = ProfessionalFamilyCreateDTO(),
    errors: Map<FormField, String?>? = null,
) {
    /*
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

     */
}