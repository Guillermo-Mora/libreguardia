package com.libreguardia.service

import com.libreguardia.config.BuildingNotFoundException
import com.libreguardia.dto.BuildingRequestDTO
import com.libreguardia.dto.BuildingResponseDTO
import com.libreguardia.repository.BuildingRepository
import java.util.UUID

class BuildingService(
    private val repository: BuildingRepository
) {
    fun getAllBuildings(): List<BuildingResponseDTO> = repository.all()

    fun getBuilding(uuid: UUID): BuildingResponseDTO = repository.findByUUID(uuid) ?: throw BuildingNotFoundException(uuid.toString())

    fun createBuilding(request: BuildingRequestDTO) {
        repository.save(request)
    }

    fun editBuilding(buildingUUID: UUID, buildingEditDTO: BuildingRequestDTO) {
        val existingBuilding = repository.findByUUID(buildingUUID) ?: throw BuildingNotFoundException(buildingUUID.toString())
        repository.update(buildingUUID, buildingEditDTO)
    }

    fun deleteBuilding(buildingUUID: UUID) {
        val existingBuilding = repository.findByUUID(buildingUUID) ?: throw BuildingNotFoundException(buildingUUID.toString())
        repository.softDelete(buildingUUID)
    }
}