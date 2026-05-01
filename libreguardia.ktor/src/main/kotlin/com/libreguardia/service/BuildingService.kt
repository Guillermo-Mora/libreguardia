package com.libreguardia.service

import com.libreguardia.dto.BuildingCreateDTO
import com.libreguardia.dto.BuildingEditDTO
import com.libreguardia.dto.BuildingResponseDTO
import com.libreguardia.exception.BuildingNotFoundException
import com.libreguardia.repository.BuildingRepository
import com.libreguardia.util.withTransaction

class BuildingService(
    private val repository: BuildingRepository
) {
    suspend fun getAll(): List<BuildingResponseDTO> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: java.util.UUID): BuildingResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw BuildingNotFoundException()

    suspend fun create(dto: BuildingCreateDTO) {
        withTransaction { repository.save(dto) }
    }

    suspend fun update(uuid: java.util.UUID, dto: BuildingEditDTO) {
        withTransaction {
            if (!repository.update(uuid, dto)) throw BuildingNotFoundException()
        }
    }

    suspend fun delete(uuid: java.util.UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw BuildingNotFoundException()
        }
    }

    suspend fun toggleEnabled(uuid: java.util.UUID, enabled: Boolean) {
        withTransaction {
            if (!repository.toggleEnabled(uuid, enabled)) throw BuildingNotFoundException()
        }
    }
}