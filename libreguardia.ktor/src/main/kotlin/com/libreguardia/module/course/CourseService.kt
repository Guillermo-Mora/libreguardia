package com.libreguardia.module.course

import com.libreguardia.frontend.component.main.create.CourseCreateField
import com.libreguardia.frontend.component.main.edit.CourseEditField
import com.libreguardia.module.professional_family.ProfessionalFamilyRepository
import com.libreguardia.module.shared.service.containsErrors
import com.libreguardia.module.shared.database.withTransaction
import com.libreguardia.module.shared.service.OperationResult
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class CourseService(
    private val courseRepository: CourseRepository,
    private val professionalFamilyRepository: ProfessionalFamilyRepository
) {
    suspend fun getAll(): List<CourseModel> = withTransaction { courseRepository.getAll() }

    suspend fun getThis(
        uuid: UUID
    ) =
        withTransaction { courseRepository.getThis(uuid) } ?: throw NotFoundException()

    suspend fun create(
        courseCreateDTO: CourseCreateDTO
    ): OperationResult {
        val errors = courseCreateDTO.validate()
        if (errors.containsErrors()) return OperationResult.Error(errors)
        return withTransaction {
            if (courseRepository.isNameTaken(
                    name = courseCreateDTO.name
                )
            ) errors[CourseCreateField.NAME] = "Name already taken"
            if (!professionalFamilyRepository.exists(
                    uuid = UUID.fromString(courseCreateDTO.professionalFamilyId)
                )
            ) errors[CourseCreateField.PROFESSIONAL_FAMILY] = "This professional family doesn't exists"
            if (errors.containsErrors()) return@withTransaction OperationResult.Error(errors)
            courseRepository.save(
                courseCreateDTO = courseCreateDTO
            )
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun editThis(
        uuid: UUID,
        courseEditDTO: CourseEditDTO
    ): OperationResult {
        val errors = courseEditDTO.validate()
        if (errors.containsErrors()) return OperationResult.Error(errors)
        return withTransaction {
            if (courseRepository.isNameTaken(
                    uuid = uuid,
                    name = courseEditDTO.name
                )
            ) errors[CourseEditField.NAME] = "Name already taken"
            if (!professionalFamilyRepository.exists(UUID.fromString(courseEditDTO.professionalFamilyId)))
                errors[CourseCreateField.PROFESSIONAL_FAMILY] = "This professional family doesn't exists"
            if (errors.containsErrors()) return@withTransaction OperationResult.Error(errors)

            if (!courseRepository.editThis(
                    uuid = uuid,
                    dto = courseEditDTO
                )
            ) throw NotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun deleteThis(uuid: UUID) {
        withTransaction {
            if (!courseRepository.deleteThis(uuid)) throw NotFoundException()
        }
    }
}