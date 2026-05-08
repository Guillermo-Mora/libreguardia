package com.libreguardia.frontend.component.main.edit

import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.*

enum class AcademicYearEditField(override val key: String) : FormField {
    NAME("name"),
    START_DATE("start-date"),
    END_DATE("end-date");
}

fun FlowContent.academicYearEdit(
    academicYear: AcademicYearEditDTO,
    errors: Map<FormField, String?>? = null,
    academicYearUuid: UUID
) {
    customForm(
        formName = "edit-academic-year",
        previousPagePath = "/academic-year",
        operationType = OperationType.Patch,
        operationPath = "/academic-year/${academicYearUuid}",
        deletePath = "/academic-year/${academicYearUuid}",
        errors = errors,
        formFields = mapOf(
            AcademicYearEditField.NAME to FormFieldData(
                text = "name",
                value = academicYear.name ?: "",
                required = true,
                inputType = InputType.text
            ),
            AcademicYearEditField.START_DATE to FormFieldData(
                text = "start date",
                value = academicYear.startDate?.toString() ?: "",
                required = true,
                inputType = InputType.date
            ),
            AcademicYearEditField.END_DATE to FormFieldData(
                text = "end date",
                value = academicYear.endDate?.toString() ?: "",
                required = true,
                inputType = InputType.date
            )
        ),
    )
}
