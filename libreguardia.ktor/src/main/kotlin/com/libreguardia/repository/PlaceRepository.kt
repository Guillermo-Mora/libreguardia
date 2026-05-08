package com.libreguardia.repository

import com.libreguardia.db.model.GroupTable
import com.libreguardia.db.model.GroupTable.pointsMultiplier
import com.libreguardia.db.model.PlaceEntity
import com.libreguardia.db.model.PlaceTable
import com.libreguardia.dto.module.PlaceDML
import com.libreguardia.model.PlaceModel
import com.libreguardia.model.toModel
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class PlaceRepository : BaseRepository<PlaceTable>(PlaceTable) {

    //Using "with" to eager load the referenced entities and prevent N + 1
    fun getAll(): List<PlaceModel> =
        PlaceEntity
            .all()
            .with(
                PlaceEntity::placeType,
                PlaceEntity::building,
                PlaceEntity::zone
            )
            .map { it.toModel() }

    //Using "load" for eager loading
    fun getThis(
        uuid: UUID
    ): PlaceModel? =
        PlaceEntity
            .findById(uuid)
            ?.load(
                PlaceEntity::placeType,
                PlaceEntity::building,
                PlaceEntity::zone
            )
            ?.toModel()

    fun isNameTaken(
        name: String
    ): Boolean =
        PlaceTable
            .select(PlaceTable.id)
            .where { PlaceTable.name eq name }
            .limit(1)
            .count() >= 1

    fun isNameTaken(
        name: String,
        uuid: UUID
    ): Boolean =
        PlaceTable
            .select(PlaceTable.id)
            .where {
                PlaceTable.id neq uuid and
                        (PlaceTable.name eq name)
            }
            .limit(1)
            .count() >= 1

    //Maybe in the future I could switch all save and update to upsert method.
    // However, the problem is that the data received for create / update can vary.
    fun save(
        model: PlaceDML
    ) {
        PlaceTable
            .insert { place ->
                place[name] = model.name
                model.floor?.let { place[floor] = it }
                model.buildingId?.let { place[building] = it }
                place[zone] = model.zoneId
                place[placeType] = model.placeTypeId
            }
    }

    fun updateThis(
        uuid: UUID,
        model: PlaceDML
    ): Boolean {
        return PlaceTable.update({ PlaceTable.id eq uuid }) { place ->
            place[name] = model.name
            place[floor] = model.floor
            place[building] = model.buildingId
            place[zone] = model.zoneId
            place[placeType] = model.placeTypeId
        } == 1
    }
}