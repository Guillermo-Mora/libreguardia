package com.libreguardia.repository

import com.libreguardia.db.model.BuildingEntity
import com.libreguardia.db.model.BuildingTable
import com.libreguardia.dto.BuildingCreateDTO
import com.libreguardia.dto.BuildingEditDTO
import com.libreguardia.dto.BuildingResponseDTO
import com.libreguardia.dto.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
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
            dto.isEnabled?.let { updated[isEnabled] = it }
        } == 1
    }

    fun delete(uuid: java.util.UUID): Boolean {
        return BuildingTable.update({ BuildingTable.id eq uuid }) {
            it[isEnabled] = false
        } == 1
    }

    fun toggleEnabled(uuid: java.util.UUID, enabled: Boolean): Boolean {
        return BuildingTable.update({ BuildingTable.id eq uuid }) {
            it[isEnabled] = enabled
        } == 1
    }
}