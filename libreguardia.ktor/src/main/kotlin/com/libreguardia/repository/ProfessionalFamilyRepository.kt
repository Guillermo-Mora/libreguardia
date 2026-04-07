package com.libreguardia.repository

import com.libreguardia.db.ProfessionalFamilyEntity
import com.libreguardia.db.ProfessionalFamilyTable
import com.libreguardia.dto.ProfessionalFamilyRequestDTO
import com.libreguardia.dto.ProfessionalFamilyResponseDTO
import com.libreguardia.exception.ProfessionalFamilyNameAlreadyExistsException
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID

class ProfessionalFamilyRepository {
    fun all(): List<ProfessionalFamilyResponseDTO> =
        ProfessionalFamilyEntity.all().map { entity ->
            ProfessionalFamilyResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                isEnabled = entity.isEnabled
            )
        }

    fun findById(id: UUID): ProfessionalFamilyResponseDTO? =
        ProfessionalFamilyEntity.findById(id)?.let { entity ->
            ProfessionalFamilyResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                isEnabled = entity.isEnabled
            )
        }

    fun findByName(name: String): ProfessionalFamilyEntity? =
        ProfessionalFamilyEntity.find { ProfessionalFamilyTable.name eq name }.firstOrNull()

    fun save(request: ProfessionalFamilyRequestDTO): ProfessionalFamilyResponseDTO {
        if (findByName(request.name) != null) {
            throw ProfessionalFamilyNameAlreadyExistsException()
        }
        val entity = ProfessionalFamilyEntity.new {
            name = request.name
            isEnabled = request.isEnabled
        }
        return ProfessionalFamilyResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            isEnabled = entity.isEnabled
        )
    }

    fun update(id: UUID, request: ProfessionalFamilyRequestDTO): ProfessionalFamilyResponseDTO? {
        val entity = ProfessionalFamilyEntity.findById(id) ?: return null
        val existingWithName = findByName(request.name)
        if (existingWithName != null && existingWithName.id.value != id) {
            throw ProfessionalFamilyNameAlreadyExistsException()
        }
        entity.name = request.name
        entity.isEnabled = request.isEnabled
        return ProfessionalFamilyResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            isEnabled = entity.isEnabled
        )
    }

    fun delete(id: UUID): Boolean {
        val entity = ProfessionalFamilyEntity.findById(id) ?: return false
        entity.delete()
        return true
    }
}
