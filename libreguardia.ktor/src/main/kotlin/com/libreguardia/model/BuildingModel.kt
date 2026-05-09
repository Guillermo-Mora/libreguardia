package com.libreguardia.model

import com.libreguardia.db.model.BuildingEntity
import java.util.UUID

data class BuildingModel(
    val id: UUID,
    val name: String
)

fun BuildingEntity.toModel() =
    BuildingModel(
        id = this.id.value,
        name = this.name
    )