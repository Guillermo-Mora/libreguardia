package com.libreguardia.db.model

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object AppSettingsTable: UUIDTable(
    name = "app_settings"
) {
    val name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
    val status = varchar(
        name = "status",
        length = 50
    )
}

class AppSettingsEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AppSettingsEntity>(AppSettingsTable)

    var name by AppSettingsTable.name
    var status by AppSettingsTable.status
}