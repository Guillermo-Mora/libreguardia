package com.libreguardia.service

import com.libreguardia.dto.module.BuildingCreateDTO
import com.libreguardia.dto.module.BuildingEditDTO
import com.libreguardia.dto.module.BuildingResponseDTO
import com.libreguardia.exception.BuildingNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.repository.BuildingRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
import java.util.UUID

class BuildingService(
    private val repository: BuildingRepository
) {
    suspend fun getAll(): List<BuildingResponseDTO> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): BuildingResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw BuildingNotFoundException()

    suspend fun create(dto: BuildingCreateDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            repository.save(dto)
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun update(uuid: UUID, dto: BuildingEditDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (!repository.update(uuid, dto)) throw BuildingNotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw BuildingNotFoundException()
        }
    }

    fun containsErrors(
        errors: MutableMap<FormField, String?>
    ) = errors.any { it.value != null }
}