package com.libreguardia.service

import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.dto.module.AcademicYearResponseDTO
import com.libreguardia.exception.AcademicYearNotFoundException
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.AcademicYearCreateField
import com.libreguardia.frontend.component.main.AcademicYearEditField
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
import java.util.UUID

class AcademicYearService(
    private val repository: AcademicYearRepository
) {
    suspend fun getAll(): List<AcademicYearResponseDTO> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): AcademicYearResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw AcademicYearNotFoundException()

    suspend fun create(dto: AcademicYearCreateDTO): OperationResult {
        val errors = dto.validate().toMutableMap()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            repository.save(dto)
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun update(uuid: UUID, dto: AcademicYearEditDTO): OperationResult {
        val errors = dto.validate().toMutableMap()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (!repository.update(uuid, dto)) throw AcademicYearNotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw AcademicYearNotFoundException()
        }
    }

    fun containsErrors(
        errors: MutableMap<FormField, String?>
    ) = errors.any { it.value != null }
}
