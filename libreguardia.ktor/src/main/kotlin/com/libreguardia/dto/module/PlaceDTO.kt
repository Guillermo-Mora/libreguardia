package com.libreguardia.dto.module

import com.libreguardia.dto.string
import com.libreguardia.frontend.component.main.create.PlaceCreateField
import com.libreguardia.frontend.component.main.edit.PlaceEditField
import com.libreguardia.model.PlaceModel
import io.ktor.http.Parameters
import java.util.UUID

data class PlaceEditDTO(
    val name: String,
    val floor: String,
    val buildingId: String,
    val zoneId: String,
    val placeTypeId: String
)

data class PlaceCreateDTO(
    val name: String = "",
    val floor: String = "",
    val buildingId: String = "",
    val zoneId: String = "",
    val placeTypeId: String = ""
)

fun PlaceModel.toPlaceEditDTO() =
    PlaceEditDTO(
        name = this.name,
        floor = this.floor ?: "",
        buildingId = this.buildingId ?: "",
        zoneId = this.zoneId,
        placeTypeId = this.placeTypeId
    )

fun Parameters.toPlaceEditDTO() =
    PlaceEditDTO(
        name = string(PlaceEditField.NAME),
        floor = string(PlaceEditField.FLOOR),
        buildingId = string(PlaceEditField.BUILDING),
        zoneId = string(PlaceEditField.ZONE),
        placeTypeId = string(PlaceEditField.PLACE_TYPE)
    )

fun Parameters.toPlaceCreateDTO() =
    PlaceCreateDTO(
        name = string(PlaceCreateField.NAME),
        floor = string(PlaceCreateField.FLOOR),
        buildingId = string(PlaceCreateField.BUILDING),
        zoneId = string(PlaceCreateField.ZONE),
        placeTypeId = string(PlaceCreateField.PLACE_TYPE)
    )

//This is the model used for the service business logic and for doing the operations in the repository.
// Since the HTML forms always return raw strings, the validation to the DTO should be done
// with strings. However, once the DTO.validate() has been called. All the data is in the correct type
// and format, so it should be mapped to their correct types. For this, I create a model,
// so the data transformation is done in a clean way, and not in the repository.
// I implemented this now for the Place table. However, I should rework the other tables and do this too.
// For now, I will leave this comment to remember that I should do this implementation in the
// other tables too.

//DML = data model
data class PlaceDML(
    val name: String,
    val floor: String?,
    val buildingId: UUID?,
    val zoneId: UUID,
    val placeTypeId: UUID
)

fun PlaceCreateDTO.toModel() =
    PlaceDML(
        name = this.name,
        floor = this.floor.takeIf { it.isNotBlank() },
        buildingId = this.buildingId.let {
            if (it.isBlank()) return@let null
            return@let UUID.fromString(it)
        },
        zoneId = UUID.fromString(this.zoneId),
        placeTypeId = UUID.fromString(this.placeTypeId)
    )

fun PlaceEditDTO.toModel() =
    PlaceDML(
        name = this.name,
        floor = this.floor.takeIf { it.isNotBlank() },
        buildingId = this.buildingId.let {
            if (it.isBlank()) return@let null
            return@let UUID.fromString(it)
        },
        zoneId = UUID.fromString(this.zoneId),
        placeTypeId = UUID.fromString(this.placeTypeId)
    )