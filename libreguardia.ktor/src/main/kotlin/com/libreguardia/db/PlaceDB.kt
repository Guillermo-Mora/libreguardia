package com.libreguardia.db

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object PlaceTable: UUIDTable(
    name = "place"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    val floor = varchar(
        name = "floor",
        length = 50
    ).nullable().default(null)
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
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
}

class PlaceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlaceEntity>(PlaceTable)

    var name by PlaceTable.name
    var floor by PlaceTable.floor
    var isEnabled by PlaceTable.isEnabled
    var building by BuildingEntity optionalReferencedOn PlaceTable.building
    var zone by ZoneEntity referencedOn PlaceTable.zone
    var placeType by PlaceEntity referencedOn PlaceTable.placeType
}