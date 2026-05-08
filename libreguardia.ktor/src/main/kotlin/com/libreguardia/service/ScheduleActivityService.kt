package com.libreguardia.service

import com.libreguardia.dto.module.ScheduleActivityCreateDTO
import com.libreguardia.dto.module.ScheduleActivityEditDTO
import com.libreguardia.dto.module.ScheduleActivityResponseDTO
import com.libreguardia.exception.ScheduleActivityNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.repository.ScheduleActivityRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
import java.util.UUID

class ScheduleActivityService(
    private val repository: ScheduleActivityRepository
) {
    suspend fun getAll(): List<ScheduleActivityResponseDTO> =
        withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): ScheduleActivityResponseDTO =
        withTransaction { repository.getByUUID(uuid) }
            ?: throw ScheduleActivityNotFoundException()

    suspend fun create(dto: ScheduleActivityCreateDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            repository.save(dto)
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun update(uuid: UUID, dto: ScheduleActivityEditDTO): OperationResult {
        val errors = dto.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (!repository.update(uuid, dto)) throw ScheduleActivityNotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw ScheduleActivityNotFoundException()
        }
    }

    fun containsErrors(
        errors: MutableMap<FormField, String?>
    ) = errors.any { it.value != null }
}

