package com.libreguardia.frontend.component.main.create

import com.libreguardia.module.course.CourseCreateDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.module.professional_family.ProfessionalFamilyModel
import kotlinx.html.FlowContent

enum class CourseCreateField(override val key: String) : FormField {
    NAME("name"),
    PROFESSIONAL_FAMILY("professional_family")
}

fun FlowContent.courseCreate(
    dto: CourseCreateDTO = CourseCreateDTO(),
    professionalFamilies: List<ProfessionalFamilyModel>,
    errors: Map<FormField, String?>? = null,
) {
    /*
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

     */
}