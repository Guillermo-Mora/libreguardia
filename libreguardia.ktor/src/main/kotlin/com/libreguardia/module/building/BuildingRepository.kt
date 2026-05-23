package com.libreguardia.module.building

import com.libreguardia.config.repository.BaseRepository
import org.jetbrains.exposed.v1.core.eq
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