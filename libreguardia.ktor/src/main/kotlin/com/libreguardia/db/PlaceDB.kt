package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object PlaceTable: UUIDTable(
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
    val building = optReference(
        name = "building_id",
        foreign = BuildingTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    ).default(null)
    val zone = reference(
        name = "zone_id",
        foreign = ZoneTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val placeType = reference(
        name = "place_type_id",
        foreign = PlaceTypeTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT,
    )

    init {
        uniqueIndex(
            customIndexName = "uq_place",
            name, building, zone
        )
    }
}