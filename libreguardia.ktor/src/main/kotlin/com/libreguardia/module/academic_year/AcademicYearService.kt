package com.libreguardia.module.academic_year

import com.libreguardia.module.shared.service.containsErrors
import com.libreguardia.module.shared.database.withTransaction
import com.libreguardia.module.shared.service.OperationResult
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class AcademicYearService(
    private val repository: AcademicYearRepository
) {
    suspend fun getAll(): List<AcademicYearModel> = withTransaction { repository.getAll() }

    suspend fun getByUUID(
        uuid: UUID
    ): AcademicYearModel =
        withTransaction { repository.getByUUID(uuid) } ?: throw NotFoundException()

    suspend fun create(
        academicYearCreateDTO: AcademicYearCreateDTO
    ): OperationResult {
        val errors = academicYearCreateDTO.validate()
        if (errors.containsErrors()) return OperationResult.Error(errors)
        return withTransaction {
            repository.save(academicYearCreateDTO)
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun update(uuid: UUID, academicYearEditDTO: AcademicYearEditDTO): OperationResult {
        val errors = academicYearEditDTO.validate()
        if (errors.containsErrors()) return OperationResult.Error(errors)
        return withTransaction {
            if (!repository.update(uuid, academicYearEditDTO)) throw NotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.deleteThis(uuid)) throw NotFoundException()
        }
    }
}