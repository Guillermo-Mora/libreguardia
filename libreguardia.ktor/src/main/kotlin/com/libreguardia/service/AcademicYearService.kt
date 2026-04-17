package com.libreguardia.service

import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.dto.AcademicYearResponseDTO
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
        if (withTransaction { repository.update(uuid, dto) }.not()) throw AcademicYearNotFoundException()
    }

    suspend fun delete(uuid: UUID) {
        if (withTransaction { repository.delete(uuid) }.not()) throw AcademicYearNotFoundException()
    }

    suspend fun toggleEnabled(uuid: UUID, enabled: Boolean) {
        if (withTransaction { repository.toggleEnabled(uuid, enabled) }.not()) throw AcademicYearNotFoundException()
    }
}
