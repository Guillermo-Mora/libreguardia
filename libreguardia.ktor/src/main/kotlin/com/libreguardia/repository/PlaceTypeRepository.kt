package com.libreguardia.repository

import com.libreguardia.db.model.PlaceTypeEntity
import com.libreguardia.db.model.PlaceTypeTable
import com.libreguardia.dto.module.PlaceTypeCreateDTO
import com.libreguardia.dto.module.PlaceTypeEditDTO
import com.libreguardia.dto.module.PlaceTypeResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class PlaceTypeRepository {
    fun getAll(): List<PlaceTypeResponseDTO> = PlaceTypeEntity.all().map { it.toResponseDTO() }

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

    fun delete(uuid: UUID): Boolean {
        return PlaceTypeTable.deleteWhere { PlaceTypeTable.id eq uuid } == 1
    }
}