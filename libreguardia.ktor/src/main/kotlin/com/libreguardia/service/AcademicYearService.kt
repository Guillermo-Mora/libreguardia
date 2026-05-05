package com.libreguardia.service

import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.dto.module.AcademicYearResponseDTO
import com.libreguardia.exception.AcademicYearNotFoundException
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.util.withTransaction
import java.util.UUID

class AcademicYearService(
    private val repository: AcademicYearRepository
) {
    suspend fun getAll(): List<AcademicYearResponseDTO> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): AcademicYearResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw AcademicYearNotFoundException()

    suspend fun create(dto: AcademicYearCreateDTO) {
        withTransaction { repository.save(dto) }
    }

    suspend fun update(uuid: UUID, dto: AcademicYearEditDTO) {
        withTransaction {
            if (!repository.update(uuid, dto)) throw AcademicYearNotFoundException()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw AcademicYearNotFoundException()
        }
    }

    suspend fun toggleEnabled(uuid: UUID, enabled: Boolean) {
        withTransaction {
            if (!repository.toggleEnabled(uuid, enabled)) throw AcademicYearNotFoundException()
        }
    }
}
