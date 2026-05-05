package com.libreguardia.repository

import com.libreguardia.db.model.BuildingEntity
import com.libreguardia.db.model.BuildingTable
import com.libreguardia.dto.module.BuildingCreateDTO
import com.libreguardia.dto.module.BuildingEditDTO
import com.libreguardia.dto.module.BuildingResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update

class BuildingRepository {
    fun getAll(): List<BuildingResponseDTO> =
        BuildingEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: java.util.UUID): BuildingResponseDTO? =
        BuildingEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: BuildingCreateDTO) {
        BuildingTable.insert {
            it[name] = dto.name
        }
    }

    fun update(uuid: java.util.UUID, dto: BuildingEditDTO): Boolean {
        return BuildingTable.update({ BuildingTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
        } == 1
    }

    fun delete(uuid: java.util.UUID): Boolean {
        return BuildingTable.deleteWhere{ BuildingTable.id eq uuid } == 1
    }
}