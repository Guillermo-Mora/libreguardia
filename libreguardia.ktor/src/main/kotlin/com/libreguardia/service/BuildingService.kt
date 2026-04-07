package com.libreguardia.service

import com.libreguardia.dto.BuildingRequestDTO
import com.libreguardia.dto.BuildingResponseDTO
import com.libreguardia.repository.BuildingRepository
import java.util.UUID

class BuildingService(
    private val repository: BuildingRepository
) {
    fun getAll(): List<BuildingResponseDTO> = repository.all()

    fun getById(id: UUID): BuildingResponseDTO? = repository.findById(id)

    fun create(request: BuildingRequestDTO): BuildingResponseDTO = repository.save(request)

    fun update(id: UUID, request: BuildingRequestDTO): BuildingResponseDTO? = repository.update(id, request)

    fun delete(id: UUID): Boolean = repository.delete(id)
}