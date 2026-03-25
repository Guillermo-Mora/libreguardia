package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ZoneTbl: UUIDTable(
   name =  "zone"
) {
    var name = varchar(
        name = "name",
        length = 50
    ).uniqueIndex()
}