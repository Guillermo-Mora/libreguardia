package com.libreguardia.module.zone

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