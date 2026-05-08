package com.libreguardia.frontend.component.main.edit

import com.libreguardia.dto.module.ScheduleActivityEditDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.*

enum class ScheduleActivityEditField(override val key: String) : FormField {
    NAME("name"),
    GENERATES_SERVICE("generates-service");
}

fun FlowContent.scheduleActivityEdit(
    activity: ScheduleActivityEditDTO,
    errors: Map<FormField, String?>? = null,
    activityUuid: UUID
) {
    customForm(
        formName = "edit-activity",
        previousPagePath = "/schedule-activity",
        operationType = OperationType.Patch,
        operationPath = "/schedule-activity/${activityUuid}",
        deletePath = "/schedule-activity/${activityUuid}",
        errors = errors,
        formFields = mapOf(
            ScheduleActivityEditField.NAME to FormFieldData(
                text = "name",
                value = activity.name ?: "",
                required = true,
                inputType = InputType.text
            ),
            ScheduleActivityEditField.GENERATES_SERVICE to FormFieldData(
                text = "generates service",
                inputType = InputType.checkBox,
                checkedValue = activity.generatesService ?: false,
                required = true
            )
        ),
    )
}
