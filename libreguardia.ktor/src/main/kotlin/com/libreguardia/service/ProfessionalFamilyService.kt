package com.libreguardia.service

import com.libreguardia.config.BCRYPT_HASH_COST
import com.libreguardia.dto.module.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.module.ProfessionalFamilyEditDTO
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.frontend.component.main.ProfessionalFamilyCreateField
import com.libreguardia.frontend.component.main.ProfessionalFamilyEditField
import com.libreguardia.frontend.component.main.UserCreateField
import com.libreguardia.frontend.component.main.UserEditField
import com.libreguardia.model.ProfessionalFamilyModel
import com.libreguardia.repository.ProfessionalFamilyRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
import io.ktor.server.plugins.NotFoundException
import java.util.UUID
import kotlin.collections.set

class ProfessionalFamilyService(
    private val professionalFamilyRepository: ProfessionalFamilyRepository
) {
    suspend fun getAll(): List<ProfessionalFamilyModel> =
        withTransaction { professionalFamilyRepository.getAll() }

    suspend fun getThis(
        uuid: UUID
    ) =
        //This exception thrown is bette than the custom ones. However, still has to be managed in status pages.
        withTransaction { professionalFamilyRepository.getThis(uuid = uuid) ?: throw NotFoundException() }

    suspend fun editThis(
        uuid: UUID,
        professionalFamilyEditDTO: ProfessionalFamilyEditDTO
    ): OperationResult {
        val errors = professionalFamilyEditDTO.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (professionalFamilyRepository.isNameTaken(
                    uuid = uuid,
                    name = professionalFamilyEditDTO.name
                )
            ) {
                errors[ProfessionalFamilyEditField.NAME] = "Email already taken"
            }
            if (containsErrors(errors)) return@withTransaction OperationResult.Error(errors)
            if (!professionalFamilyRepository.editThis(
                    uuid = uuid,
                    professionalFamilyEditDTO = professionalFamilyEditDTO
                )
            ) throw NotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun deleteThis(
        uuid: UUID
    ) {
        withTransaction { if (!professionalFamilyRepository.deleteThis(uuid = uuid)) throw NotFoundException() }
    }

    suspend fun create(
        professionalFamilyCreateDTO: ProfessionalFamilyCreateDTO
    ): OperationResult {
        val errors = professionalFamilyCreateDTO.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (professionalFamilyRepository.isNameTaken(
                    name = professionalFamilyCreateDTO.name
                )
            ) {
                errors[ProfessionalFamilyCreateField.NAME] = "Name already taken"
            }
            if (containsErrors(errors)) return@withTransaction OperationResult.Error(errors)
            professionalFamilyRepository.save(
                professionalFamilyCreateDTO = professionalFamilyCreateDTO
            )
            return@withTransaction OperationResult.Success()
        }
    }
}