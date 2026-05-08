package com.libreguardia.service

import com.libreguardia.dto.module.ScheduleActivityCreateDTO
import com.libreguardia.dto.module.ScheduleActivityEditDTO
import com.libreguardia.dto.module.ScheduleActivityResponseDTO
import com.libreguardia.exception.ScheduleActivityNotFoundException
import com.libreguardia.repository.ScheduleActivityRepository
import com.libreguardia.util.withTransaction
import java.util.UUID

class ScheduleActivityService(
    private val repository: ScheduleActivityRepository
) {
    suspend fun getAll(): List<ScheduleActivityResponseDTO> =
        withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): ScheduleActivityResponseDTO =
        withTransaction { repository.getByUUID(uuid) }
            ?: throw ScheduleActivityNotFoundException()

    suspend fun create(dto: ScheduleActivityCreateDTO) {
        withTransaction { repository.save(dto) }
    }

    suspend fun update(uuid: UUID, dto: ScheduleActivityEditDTO) {
        withTransaction {
            if (!repository.update(uuid, dto)) throw ScheduleActivityNotFoundException()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.deleteThis(uuid)) throw ScheduleActivityNotFoundException()
        }
    }
}

