package com.libreguardia.frontend.component.main.edit

import com.libreguardia.module.course.CourseEditDTO
import com.libreguardia.frontend.component.*
import com.libreguardia.module.professional_family.ProfessionalFamilyModel
import kotlinx.html.FlowContent
import java.util.*

enum class CourseEditField(override val key: String) : FormField {
    NAME("name"),
    PROFESSIONAL_FAMILY("professional_family")
}

fun FlowContent.courseEdit(
    dto: CourseEditDTO,
    professionalFamilies: List<ProfessionalFamilyModel>,
    errors: Map<FormField, String?>? = null,
    uuid: UUID
) {
    /*
    customForm(
        formName = "edit-course",
        previousPagePath = "/course",
        operationType = OperationType.Patch,
        operationPath = "/course/${uuid}",
        deletePath = "/course/${uuid}",
        errors = errors,
        formFields = mapOf(
            CourseEditField.NAME to FormFieldData(
                text = "name",
                value = dto.name,
                required = true,
                inputType = InputType.text
            ),
            CourseEditField.PROFESSIONAL_FAMILY to FormFieldData(
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