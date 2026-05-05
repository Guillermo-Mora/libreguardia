package com.libreguardia.service

import com.libreguardia.dto.module.CourseCreateDTO
import com.libreguardia.dto.module.CourseEditDTO
import com.libreguardia.dto.module.CourseResponseDTO
import com.libreguardia.exception.CourseNotFoundException
import com.libreguardia.exception.ProfessionalFamilyNotFoundException
import com.libreguardia.repository.CourseRepository
import com.libreguardia.util.withTransaction
import java.util.UUID

class CourseService(
    private val repository: CourseRepository
) {
    suspend fun getAll(): List<CourseResponseDTO> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): CourseResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw CourseNotFoundException()

    suspend fun create(dto: CourseCreateDTO) {
        withTransaction {
            if (!repository.professionalFamilyExists(dto.professionalFamilyId)) {
                throw ProfessionalFamilyNotFoundException()
            }
            repository.save(dto)
        }
    }

    suspend fun update(uuid: UUID, dto: CourseEditDTO) {
        withTransaction {
            if (!repository.update(uuid, dto)) throw CourseNotFoundException()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw CourseNotFoundException()
        }
    }

    suspend fun toggleEnabled(uuid: UUID, enabled: Boolean) {
        withTransaction {
            if (!repository.toggleEnabled(uuid, enabled)) throw CourseNotFoundException()
        }
    }
}