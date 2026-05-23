package com.libreguardia.frontend.component.main.edit

import com.libreguardia.module.place.PlaceEditDTO
import com.libreguardia.frontend.component.*
import com.libreguardia.module.building.BuildingModel
import com.libreguardia.module.place_type.PlaceTypeModel
import com.libreguardia.module.zone.ZoneModel
import kotlinx.html.FlowContent
import java.util.*

enum class PlaceEditField(override val key: String) : FormField {
    NAME("name"),
    FLOOR("floor"),
    PLACE_TYPE("place_type"),
    BUILDING("building"),
    ZONE("zone")
}

fun FlowContent.placeEdit(
    dto: PlaceEditDTO,
    placeTypes: List<PlaceTypeModel>,
    buildings: List<BuildingModel>,
    zones: List<ZoneModel>,
    errors: Map<FormField, String?>? = null,
    uuid: UUID
) {
    /*
    customForm(
        formName = "edit-place",
        previousPagePath = "/place",
        operationType = OperationType.Patch,
        operationPath = "/place/${uuid}",
        deletePath = "/place/${uuid}",
        errors = errors,
        formFields = mapOf(
            PlaceEditField.NAME to FormFieldData(
                text = "name",
                value = dto.name,
                required = true,
                inputType = InputType.text
            ),
            PlaceEditField.PLACE_TYPE to FormFieldData(
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
            PlaceEditField.BUILDING to FormFieldData(
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
            PlaceEditField.FLOOR to FormFieldData(
                text = "floor",
                value = dto.floor,
                required = false,
                inputType = InputType.text
            ),
            PlaceEditField.ZONE to FormFieldData(
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

     */
}