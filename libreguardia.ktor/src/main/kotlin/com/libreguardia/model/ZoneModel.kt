package com.libreguardia.model

import com.libreguardia.db.model.ZoneEntity
import java.util.UUID

data class ZoneModel(
    val id: UUID,
    val name: String
)

fun ZoneEntity.toModel() =
    ZoneModel(
        id = this.id.value,
        name = this.name
    )