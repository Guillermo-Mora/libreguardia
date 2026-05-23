package com.libreguardia.module.schedule_activity

import com.libreguardia.config.ScheduleActivityNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.util.withTransaction
import com.libreguardia.config.service.OperationResult
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
            if (!repository.deleteThis(uuid)) throw ScheduleActivityNotFoundException()
        }
    }

    fun containsErrors(
        errors: MutableMap<FormField, String?>
    ) = errors.any { it.value != null }
}