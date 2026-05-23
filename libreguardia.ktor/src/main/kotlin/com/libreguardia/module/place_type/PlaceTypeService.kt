package com.libreguardia.module.place_type

import com.libreguardia.module.shared.exception.PlaceTypeNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.module.shared.database.withTransaction
import com.libreguardia.module.shared.service.OperationResult
import java.util.UUID

class PlaceTypeService(
    private val repository: PlaceTypeRepository
) {
    suspend fun getAll(): List<PlaceTypeModel> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): PlaceTypeResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw PlaceTypeNotFoundException()

    suspend fun create(dto: PlaceTypeCreateDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            repository.save(dto)
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun update(uuid: UUID, dto: PlaceTypeEditDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (!repository.update(uuid, dto)) throw PlaceTypeNotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.deleteThis(uuid)) throw PlaceTypeNotFoundException()
        }
    }

    fun containsErrors(
        errors: MutableMap<FormField, String?>
    ) = errors.any { it.value != null }
}