package com.libreguardia.frontend.component.main

import com.libreguardia.dto.module.ZoneEditDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.*

enum class ZoneEditField(override val key: String) : FormField {
    NAME("name");
}

fun FlowContent.zoneEdit(
    zone: ZoneEditDTO,
    errors: Map<FormField, String?>? = null,
    zoneUuid: UUID
) {
    customForm(
        formName = "edit-zone",
        previousPagePath = "/zone",
        operationType = OperationType.Patch,
        operationPath = "/zone/${zoneUuid}",
        deletePath = "/zone/${zoneUuid}",
        errors = errors,
        formFields = mapOf(
            ZoneEditField.NAME to FormFieldData(
                text = "name",
                value = zone.name ?: "",
                required = true,
                inputType = InputType.text
            )
        ),
    )
}
