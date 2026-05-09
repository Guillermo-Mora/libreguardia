package com.libreguardia.frontend.component.main

import com.libreguardia.dto.module.ZoneCreateDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class ZoneCreateField(override val key: String) : FormField {
    NAME("name");
}

fun FlowContent.zoneCreate(
    zone: ZoneCreateDTO = ZoneCreateDTO(""),
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "create-zone",
        previousPagePath = "/zone",
        operationPath = "/zone",
        operationType = OperationType.Post,
        errors = errors,
        formFields = mapOf(
            ZoneCreateField.NAME to FormFieldData(
                text = "name",
                value = zone.name,
                required = true,
                inputType = InputType.text
            )
        ),
    )
}
