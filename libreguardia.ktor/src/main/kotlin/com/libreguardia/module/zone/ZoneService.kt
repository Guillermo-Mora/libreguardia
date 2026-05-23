package com.libreguardia.module.zone

import com.libreguardia.config.ZoneNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.util.withTransaction
import com.libreguardia.config.service.OperationResult
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