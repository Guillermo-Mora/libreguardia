package com.libreguardia.module.place_type

import com.libreguardia.module.shared.repository.BaseRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class PlaceTypeRepository : BaseRepository<PlaceTypeTable>(PlaceTypeTable) {
    fun getAll(): List<PlaceTypeModel> = PlaceTypeEntity.all().map { it.toModel() }

    fun getByUUID(uuid: UUID): PlaceTypeResponseDTO? =
        PlaceTypeEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: PlaceTypeCreateDTO) {
        PlaceTypeTable.insert {
            it[name] = dto.name
        }
    }

    fun update(uuid: UUID, dto: PlaceTypeEditDTO): Boolean {
        return PlaceTypeTable.update({ PlaceTypeTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
        } == 1
    }
}