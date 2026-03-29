package com.libreguardia.db

import com.libreguardia.config.UUIDSerializer
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
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
}

class ZoneEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ZoneEntity>(ZoneTable)

    var name by ZoneTable.name
    var isEnabled by ZoneTable.isEnabled
    val places by PlaceEntity referrersOn PlaceTable.zone
}

fun etyToModel(dao: ZoneEntity) = ZoneMdl(
    dao.id.value,
    dao.name
)

@Serializable
data class ZoneMdl(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
)