package com.libreguardia.frontend.component.main.create

import com.libreguardia.dto.module.ScheduleActivityCreateDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class ScheduleActivityCreateField(override val key: String) : FormField {
    NAME("name"),
    GENERATES_SERVICE("generates-service");
}

fun FlowContent.scheduleActivityCreate(
    activity: ScheduleActivityCreateDTO = ScheduleActivityCreateDTO("", false),
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "create-activity",
        previousPagePath = "/schedule-activity",
        operationPath = "/schedule-activity",
        operationType = OperationType.Post,
        errors = errors,
        formFields = mapOf(
            ScheduleActivityCreateField.NAME to FormFieldData(
                text = "name",
                value = activity.name,
                required = true,
                inputType = InputType.text
            ),
            ScheduleActivityCreateField.GENERATES_SERVICE to FormFieldData(
                text = "generates service",
                inputType = InputType.checkBox,
                checkedValue = activity.generatesService,
                required = true
            )
        ),
    )
}
