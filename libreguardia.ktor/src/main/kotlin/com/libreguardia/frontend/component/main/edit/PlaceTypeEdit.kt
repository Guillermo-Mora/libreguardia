package com.libreguardia.frontend.component.main.edit

import com.libreguardia.dto.module.PlaceTypeEditDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.UUID

enum class PlaceTypeEditField(override val key: String) : FormField {
    NAME("name");
}

fun FlowContent.placeTypeEdit(
    placeType: PlaceTypeEditDTO,
    errors: Map<FormField, String?>? = null,
    placeTypeUuid: UUID
) {
    customForm(
        formName = "edit-place-type",
        previousPagePath = "/place-type",
        operationType = OperationType.Patch,
        operationPath = "/place-type/${placeTypeUuid}",
        deletePath = "/place-type/${placeTypeUuid}",
        errors = errors,
        formFields = mapOf(
            PlaceTypeEditField.NAME to FormFieldData(
                text = "name",
                value = placeType.name ?: "",
                required = true,
                inputType = InputType.text
            ),
        ),
    )
}