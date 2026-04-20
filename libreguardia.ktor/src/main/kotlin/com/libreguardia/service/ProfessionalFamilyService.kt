package com.libreguardia.service

import com.libreguardia.dto.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.ProfessionalFamilyEditDTO
import com.libreguardia.dto.ProfessionalFamilyResponseDTO
import com.libreguardia.exception.ProfessionalFamilyNotFoundException
import com.libreguardia.repository.ProfessionalFamilyRepository
import com.libreguardia.util.withTransaction
import java.util.UUID

class ProfessionalFamilyService(
    private val repository: ProfessionalFamilyRepository
) {
    suspend fun getAll(): List<ProfessionalFamilyResponseDTO> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): ProfessionalFamilyResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw ProfessionalFamilyNotFoundException()

    suspend fun create(dto: ProfessionalFamilyCreateDTO) = withTransaction { repository.save(dto) }

    suspend fun update(uuid: UUID, dto: ProfessionalFamilyEditDTO) {
        if (withTransaction { repository.update(uuid, dto) }) return
        throw ProfessionalFamilyNotFoundException()
    }

    suspend fun delete(uuid: UUID) {
        if (withTransaction { repository.delete(uuid) }) return
        throw ProfessionalFamilyNotFoundException()
    }

    suspend fun toggleEnabled(uuid: UUID, enabled: Boolean) {
        if (withTransaction { repository.toggleEnabled(uuid, enabled) }) return
        throw ProfessionalFamilyNotFoundException()
    }
}