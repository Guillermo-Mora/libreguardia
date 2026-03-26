package com.libreguardia.db

import org.jetbrains.exposed.v1.core.Table

object AppSettingsTable: Table(
    name = "app_settings"
) {
    val name = varchar(
        name = "name",
        length = 50
    )
    val status = varchar(
        name = "status",
        length = 50
    )
    override val primaryKey = PrimaryKey(
        firstColumn = name
    )

}