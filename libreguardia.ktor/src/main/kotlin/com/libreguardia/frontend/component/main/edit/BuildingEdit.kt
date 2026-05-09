package com.libreguardia.frontend.component.main.edit

import com.libreguardia.dto.module.BuildingEditDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.*

enum class BuildingEditField(override val key: String) : FormField {
    NAME("name");
}

fun FlowContent.buildingEdit(
    building: BuildingEditDTO,
    errors: Map<FormField, String?>? = null,
    buildingUuid: UUID
) {
    customForm(
        formName = "edit-building",
        previousPagePath = "/building",
        operationType = OperationType.Patch,
        operationPath = "/building/${buildingUuid}",
        deletePath = "/building/${buildingUuid}",
        errors = errors,
        formFields = mapOf(
            BuildingEditField.NAME to FormFieldData(
                text = "name",
                value = building.name ?: "",
                required = true,
                inputType = InputType.text
            )
        ),
    )
}
