package com.libreguardia.repository

import com.libreguardia.db.model.BuildingEntity
import com.libreguardia.db.model.BuildingTable
import com.libreguardia.dto.module.BuildingCreateDTO
import com.libreguardia.dto.module.BuildingEditDTO
import com.libreguardia.dto.module.BuildingResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import com.libreguardia.model.BuildingModel
import com.libreguardia.model.toModel
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class BuildingRepository : BaseRepository<BuildingTable>(BuildingTable) {
    fun getAll(): List<BuildingModel> =
        BuildingEntity.all().map { it.toModel() }

    fun getByUUID(uuid: UUID): BuildingResponseDTO? =
        BuildingEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: BuildingCreateDTO) {
        BuildingTable.insert {
            it[name] = dto.name
        }
    }

    fun update(uuid: UUID, dto: BuildingEditDTO): Boolean {
        return BuildingTable.update({ BuildingTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
        } == 1
    }
}