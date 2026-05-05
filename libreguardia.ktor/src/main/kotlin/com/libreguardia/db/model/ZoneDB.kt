package com.libreguardia.db.model

import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.*

object ZoneTable: UUIDTable(
   name =  "zone"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}

class ZoneEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ZoneEntity>(ZoneTable)

    var name by ZoneTable.name
    val places by PlaceEntity referrersOn PlaceTable.zone
}