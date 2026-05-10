package com.libreguardia.service

import com.libreguardia.dto.module.PlaceTypeCreateDTO
import com.libreguardia.dto.module.PlaceTypeEditDTO
import com.libreguardia.dto.module.PlaceTypeResponseDTO
import com.libreguardia.exception.PlaceTypeNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.model.PlaceTypeModel
import com.libreguardia.repository.PlaceTypeRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
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