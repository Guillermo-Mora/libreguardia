package com.libreguardia.db.model

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object BuildingTable: UUIDTable(
    name = "building"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}

class BuildingEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BuildingEntity>(BuildingTable)

    var name by BuildingTable.name
    val places by PlaceEntity optionalReferrersOn PlaceTable.building
}