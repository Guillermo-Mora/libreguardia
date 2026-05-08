package com.libreguardia.model

import com.libreguardia.db.model.PlaceEntity

data class PlaceScheduleModel(
    val fullName: String,
    val building: String?,
    val floor: String?
)

data class PlaceModel(
    val id: String,
    val name: String,
    val floor: String?,
    val buildingId: String?,
    val buildingName: String?,
    val zoneId: String,
    val zoneName: String,
    val placeTypeId: String,
    val placeTypeName: String
)

fun PlaceEntity.toModel() =
    PlaceModel(
        id = this.id.value.toString(),
        name = this.name,
        floor = this.floor,
        buildingId = this.building?.id?.value.toString(),
        buildingName = this.building?.name,
        zoneId = this.zone.id.value.toString(),
        zoneName = this.zone.name,
        placeTypeId = this.placeType.id.value.toString(),
        placeTypeName = this.placeType.name
    )