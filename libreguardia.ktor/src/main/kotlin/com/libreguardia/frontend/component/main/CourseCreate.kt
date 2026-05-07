package com.libreguardia.frontend.component.main

import com.libreguardia.dto.module.CourseCreateDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.FormFieldData
import com.libreguardia.frontend.component.OperationType
import com.libreguardia.frontend.component.SelectOption
import com.libreguardia.frontend.component.customForm
import com.libreguardia.model.ProfessionalFamilyModel
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class CourseCreateField(override val key: String) : FormField {
    NAME("name"),
    PROFESSIONAL_FAMILY("professional_family")
}

fun FlowContent.courseCreate(
    dto: CourseCreateDTO = CourseCreateDTO(),
    professionalFamilies: List<ProfessionalFamilyModel>,
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "edit-course",
        previousPagePath = "/course",
        operationType = OperationType.Post,
        operationPath = "/course",
        errors = errors,
        formFields = mapOf(
            CourseCreateField.NAME to FormFieldData(
                text = "name",
                value = dto.name,
                required = true,
                inputType = InputType.text
            ),
            CourseCreateField.PROFESSIONAL_FAMILY to FormFieldData(
                text = "professional family",
                required = true,
                selectOptions =
                    professionalFamilies.map { professionalFamily ->
                        SelectOption(
                            text = professionalFamily.name,
                            id = professionalFamily.id,
                            selected = professionalFamily.id == dto.professionalFamilyId
                        )
                    }
            ),
        ),
    )
}