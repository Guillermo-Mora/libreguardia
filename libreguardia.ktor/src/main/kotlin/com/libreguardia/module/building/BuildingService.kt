package com.libreguardia.module.building

import com.libreguardia.config.exception.BuildingNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.config.util.withTransaction
import com.libreguardia.config.service.OperationResult
import java.util.UUID

class BuildingService(
    private val repository: BuildingRepository
) {
    suspend fun getAll(): List<BuildingModel> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): BuildingResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw BuildingNotFoundException()

    suspend fun create(buildingCreateDTO: BuildingCreateDTO): OperationResult {
        val errors = buildingCreateDTO.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            repository.save(buildingCreateDTO)
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun update(uuid: UUID, buildingEditDTO: BuildingEditDTO): OperationResult {
        val errors = buildingEditDTO.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (!repository.update(uuid, buildingEditDTO)) throw BuildingNotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.deleteThis(uuid)) throw BuildingNotFoundException()
        }
    }

    fun containsErrors(
        errors: MutableMap<FormField, String?>
    ) = errors.any { it.value != null }
}