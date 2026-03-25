package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object PlaceTbl: UUIDTable("place") {
    var name = varchar("name", 50)
    var floor = varchar("floor", 50).nullable().default(null)
    var buildingId = optReference("building_id", BuildingTbl.id).default(null)
    var zoneId = reference("zone_id", ZoneTbl.id)
    var placeTypeId = reference("place_type_id", PlaceTypeTbl.id)

    init {
        uniqueIndex("uq_place", name, buildingId, zoneId)
    }
}