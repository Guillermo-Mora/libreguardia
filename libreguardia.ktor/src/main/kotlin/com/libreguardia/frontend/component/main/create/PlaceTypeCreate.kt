package com.libreguardia.frontend.component.main.create

import com.libreguardia.module.place_type.PlaceTypeCreateDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent

enum class PlaceTypeCreateField(override val key: String) : FormField {
    NAME("name");
}

fun FlowContent.placeTypeCreate(
    placeType: PlaceTypeCreateDTO = PlaceTypeCreateDTO(name = ""),
    errors: Map<FormField, String?>? = null,
) {
    /*
    customForm(
        formName = "create-place-type",
        previousPagePath = "/place-type",
        operationPath = "/place-type",
        operationType = OperationType.Post,
        errors = errors,
        formFields = mapOf(
            PlaceTypeCreateField.NAME to FormFieldData(
                text = "name",
                value = placeType.name,
                required = true,
                inputType = InputType.text
            ),
        ),
    )

     */
}