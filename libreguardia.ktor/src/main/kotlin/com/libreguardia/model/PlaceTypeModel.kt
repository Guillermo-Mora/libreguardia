package com.libreguardia.model

import com.libreguardia.db.model.PlaceTypeEntity
import java.util.*

data class PlaceTypeModel(
    val id: UUID,
    val name: String
)

fun PlaceTypeEntity.toModel() =
    PlaceTypeModel(
        id = this.id.value,
        name = this.name
    )