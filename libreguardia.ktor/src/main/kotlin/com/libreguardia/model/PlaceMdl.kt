package com.libreguardia.model

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object PlaceTbl: UUIDTable(
    name = "place"
) {
    val name = varchar(
        name = "name",
        length = 50
    )
    val floor = varchar(
        name = "floor",
        length = 50
    ).nullable().default(null)
    val buildingId = optReference(
        name = "building_id",
        refColumn = BuildingTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    ).default(null)
    val zoneId = reference(
        name = "zone_id",
        refColumn = ZoneTbl.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val placeTypeId = reference(
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