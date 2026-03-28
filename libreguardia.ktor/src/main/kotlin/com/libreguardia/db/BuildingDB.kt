package com.libreguardia.db

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
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
}

class BuildingEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BuildingEntity>(BuildingTable)

    var name by BuildingTable.name
    var isEnabled by BuildingTable.isEnabled
}