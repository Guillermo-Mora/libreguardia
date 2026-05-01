package com.libreguardia.service

import com.libreguardia.dto.ZoneCreateDTO
import com.libreguardia.dto.ZoneEditDTO
import com.libreguardia.dto.ZoneResponseDTO
import com.libreguardia.exception.ZoneNotFoundException
import com.libreguardia.repository.ZoneRepository
import com.libreguardia.util.withTransaction
import java.util.UUID

class ZoneService(
    private val repository: ZoneRepository
) {
    suspend fun getAll(): List<ZoneResponseDTO> = withTransaction { repository.getAll() }

    suspend fun create(dto: ZoneCreateDTO) {
        withTransaction { repository.save(dto) }
    }

    suspend fun update(uuid: UUID, dto: ZoneEditDTO) {
        withTransaction {
            if (!repository.update(uuid, dto)) throw ZoneNotFoundException()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw ZoneNotFoundException()
        }
    }
}