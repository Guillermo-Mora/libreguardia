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

    fun findById(id: UUID): BuildingResponseDTO? =
        BuildingEntity.findById(id)?.let { entity ->
            BuildingResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                isEnabled = entity.isEnabled
            )
        }

    fun save(request: BuildingRequestDTO): BuildingResponseDTO {
        val entity = BuildingEntity.new {
            name = request.name
            isEnabled = request.isEnabled
        }
        return BuildingResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            isEnabled = entity.isEnabled
        )
    }

    fun update(id: UUID, request: BuildingRequestDTO): BuildingResponseDTO? {
        val entity = BuildingEntity.findById(id) ?: return null
        entity.name = request.name
        entity.isEnabled = request.isEnabled
        return BuildingResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            isEnabled = entity.isEnabled
        )
    }

    fun delete(id: UUID): Boolean {
        val entity = BuildingEntity.findById(id) ?: return false
        entity.delete()
        return true
    }
}