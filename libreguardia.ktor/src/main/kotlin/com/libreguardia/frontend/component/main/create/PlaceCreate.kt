package com.libreguardia.frontend.component.main.create

import com.libreguardia.dto.module.PlaceCreateDTO
import com.libreguardia.frontend.component.*
import com.libreguardia.model.BuildingModel
import com.libreguardia.model.PlaceTypeModel
import com.libreguardia.model.ZoneModel
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class PlaceCreateField(override val key: String) : FormField {
    NAME("name"),
    FLOOR("floor"),
    PLACE_TYPE("place_type"),
    BUILDING("building"),
    ZONE("zone")
}

fun FlowContent.placeCreate(
    dto: PlaceCreateDTO = PlaceCreateDTO(),
    placeTypes: List<PlaceTypeModel>,
    buildings: List<BuildingModel>,
    zones: List<ZoneModel>,
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "edit-place",
        previousPagePath = "/place",
        operationType = OperationType.Post,
        operationPath = "/place",
        errors = errors,
        formFields = mapOf(
            PlaceCreateField.NAME to FormFieldData(
                text = "name",
                value = dto.name,
                required = true,
                inputType = InputType.text
            ),
            PlaceCreateField.PLACE_TYPE to FormFieldData(
                text = "place type",
                required = true,
                selectOptions =
                    placeTypes.map { placeType ->
                        SelectOption(
                            text = placeType.name,
                            id = placeType.id.toString(),
                            selected = placeType.id.toString() == dto.placeTypeId
                        )
                    }
            ),
            PlaceCreateField.BUILDING to FormFieldData(
                text = "building",
                required = false,
                selectOptions = listOf(
                    SelectOption(
                        text = "None",
                        id = "",
                        selected = dto.buildingId == ""
                    )
                ) + buildings.map { building ->
                    SelectOption(
                        text = building.name,
                        id = building.id.toString(),
                        selected = building.id.toString() == dto.buildingId
                    )
                }
            ),
            PlaceCreateField.FLOOR to FormFieldData(
                text = "floor",
                value = dto.floor,
                required = false,
                inputType = InputType.text
            ),
            PlaceCreateField.ZONE to FormFieldData(
                text = "zone",
                required = true,
                selectOptions =
                    zones.map { zone ->
                        SelectOption(
                            text = zone.name,
                            id = zone.id.toString(),
                            selected = zone.id.toString() == dto.zoneId
                        )
                    }
            ),
        ),
    )
}