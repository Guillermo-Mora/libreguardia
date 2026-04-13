package com.libreguardia.db.model

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object PlaceTypeTable: UUIDTable(
    name = "place_type"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
}

class PlaceTypeEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlaceTypeEntity>(PlaceTypeTable)

    var name by PlaceTypeTable.name
    var isEnabled by PlaceTypeTable.isEnabled
    val places by PlaceEntity referrersOn PlaceTable.placeType
}