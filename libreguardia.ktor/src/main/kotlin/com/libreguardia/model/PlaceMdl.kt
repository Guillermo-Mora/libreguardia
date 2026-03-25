package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object PlaceTbl: UUIDTable(
    name = "place"
) {
    var name = varchar(
        name = "name",
        length = 50
    )
    var floor = varchar(
        name = "floor",
        length = 50
    ).nullable().default(null)
    var buildingId = optReference(
        name = "building_id",
        refColumn = BuildingTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    ).default(null)
    var zoneId = reference(
        name = "zone_id",
        refColumn = ZoneTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    var placeTypeId = reference(
        name = "place_type_id",
        refColumn = PlaceTypeTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_place",
            name, buildingId, zoneId
        )
    }
}