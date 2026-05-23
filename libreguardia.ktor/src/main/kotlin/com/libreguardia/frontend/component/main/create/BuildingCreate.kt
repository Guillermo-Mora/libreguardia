package com.libreguardia.frontend.component.main.create

import com.libreguardia.module.building.BuildingCreateDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent

enum class BuildingCreateField(override val key: String) : FormField {
    NAME("name");
}

fun FlowContent.buildingCreate(
    building: BuildingCreateDTO = BuildingCreateDTO(""),
    errors: Map<FormField, String?>? = null,
) {
    /*
    customForm(
        formName = "create-building",
        previousPagePath = "/building",
        operationPath = "/building",
        operationType = OperationType.Post,
        errors = errors,
        formFields = mapOf(
            BuildingCreateField.NAME to FormFieldData(
                text = "name",
                value = building.name,
                required = true,
                inputType = InputType.text
            )
        ),
    )

     */
}
