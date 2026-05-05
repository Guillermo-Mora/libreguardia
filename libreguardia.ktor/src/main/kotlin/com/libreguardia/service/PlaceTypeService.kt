package com.libreguardia.service

import com.libreguardia.dto.module.PlaceTypeCreateDTO
import com.libreguardia.dto.module.PlaceTypeEditDTO
import com.libreguardia.dto.module.PlaceTypeResponseDTO
import com.libreguardia.exception.PlaceTypeNotFoundException
import com.libreguardia.repository.PlaceTypeRepository
import com.libreguardia.util.withTransaction
import java.util.UUID

class PlaceTypeService(
    private val placeTypeRepository: PlaceTypeRepository
) {
    suspend fun getAll(): List<PlaceTypeResponseDTO> = withTransaction { placeTypeRepository.getAll() }

    suspend fun getByUUID(uuid: UUID): PlaceTypeResponseDTO =
        withTransaction { placeTypeRepository.getByUUID(uuid) } ?: throw PlaceTypeNotFoundException()

    suspend fun create(dto: PlaceTypeCreateDTO) {
        withTransaction { placeTypeRepository.save(dto) }
    }

    suspend fun update(uuid: UUID, dto: PlaceTypeEditDTO) {
        withTransaction {
            if (!placeTypeRepository.update(uuid, dto)) throw PlaceTypeNotFoundException()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!placeTypeRepository.delete(uuid)) throw PlaceTypeNotFoundException()
        }
    }
}