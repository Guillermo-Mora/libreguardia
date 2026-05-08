package com.libreguardia.frontend.component.main

import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class AcademicYearCreateField(override val key: String) : FormField {
    NAME("name"),
    START_DATE("start-date"),
    END_DATE("end-date");
}

fun FlowContent.academicYearCreate(
    academicYear: AcademicYearCreateDTO = AcademicYearCreateDTO("", kotlinx.datetime.LocalDate(2000, 1, 1), kotlinx.datetime.LocalDate(2000, 12, 31)),
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "create-academic-year",
        previousPagePath = "/academic-year",
        operationPath = "/academic-year",
        operationType = OperationType.Post,
        errors = errors,
        formFields = mapOf(
            AcademicYearCreateField.NAME to FormFieldData(
                text = "name",
                value = academicYear.name,
                required = true,
                inputType = InputType.text
            ),
            AcademicYearCreateField.START_DATE to FormFieldData(
                text = "start date",
                value = academicYear.startDate.toString(),
                required = true,
                inputType = InputType.date
            ),
            AcademicYearCreateField.END_DATE to FormFieldData(
                text = "end date",
                value = academicYear.endDate.toString(),
                required = true,
                inputType = InputType.date
            )
        ),
    )
}
