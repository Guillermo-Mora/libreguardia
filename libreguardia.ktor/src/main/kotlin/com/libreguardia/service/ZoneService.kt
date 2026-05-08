package com.libreguardia.service

import com.libreguardia.dto.module.ZoneCreateDTO
import com.libreguardia.dto.module.ZoneEditDTO
import com.libreguardia.dto.module.ZoneResponseDTO
import com.libreguardia.exception.ZoneNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.model.ZoneModel
import com.libreguardia.repository.ZoneRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
import java.util.UUID

class ZoneService(
    private val repository: ZoneRepository
) {
    suspend fun getAll(): List<ZoneModel> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): ZoneResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw ZoneNotFoundException()

    suspend fun create(dto: ZoneCreateDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            repository.save(dto)
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun update(uuid: UUID, dto: ZoneEditDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (!repository.update(uuid, dto)) throw ZoneNotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.deleteThis(uuid)) throw ZoneNotFoundException()
        }
    }

    fun containsErrors(
        errors: MutableMap<FormField, String?>
    ) = errors.any { it.value != null }
}