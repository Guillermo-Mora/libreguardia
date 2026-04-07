package com.libreguardia.repository

import com.libreguardia.db.BuildingEntity
import com.libreguardia.db.BuildingTable
import com.libreguardia.dto.BuildingRequestDTO
import com.libreguardia.dto.BuildingResponseDTO
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID

class BuildingRepository {
    fun all(): List<BuildingResponseDTO> =
        BuildingEntity.all().map { entity ->
            BuildingResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                isEnabled = entity.isEnabled
            )
        }

    fun findByUUID(uuid: UUID): BuildingResponseDTO? =
        BuildingEntity.find { BuildingTable.id eq uuid }.firstOrNull()?.let { entity ->
            BuildingResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                isEnabled = entity.isEnabled
            )
        }

    fun save(request: BuildingRequestDTO) {
        BuildingEntity.new {
            name = request.name
            isEnabled = request.isEnabled
        }
    }

    fun update(buildingUUID: UUID, request: BuildingRequestDTO) {
        val entity = BuildingEntity.findById(buildingUUID) ?: return
        entity.name = request.name
        entity.isEnabled = request.isEnabled
    }

    fun softDelete(buildingUUID: UUID) {
        val entity = BuildingEntity.findById(buildingUUID) ?: return
        entity.isEnabled = false
    }

    fun delete(buildingUUID: UUID) {
        val entity = BuildingEntity.findById(buildingUUID) ?: return
        entity.delete()
    }
}