package com.libreguardia.repository

import com.libreguardia.db.CourseEntity
import com.libreguardia.db.CourseTable
import com.libreguardia.db.ProfessionalFamilyEntity
import com.libreguardia.dto.CourseRequestDTO
import com.libreguardia.dto.CourseResponseDTO
import com.libreguardia.exception.CourseNameAlreadyExistsException
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID

class CourseRepository {
    fun all(): List<CourseResponseDTO> =
        CourseEntity.all().map { entity ->
            CourseResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                isEnabled = entity.isEnabled,
                professionalFamilyId = entity.professionalFamily.id.value.toString()
            )
        }

    fun findById(id: UUID): CourseResponseDTO? =
        CourseEntity.findById(id)?.let { entity ->
            CourseResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                isEnabled = entity.isEnabled,
                professionalFamilyId = entity.professionalFamily.id.value.toString()
            )
        }

    fun findByName(name: String): CourseEntity? =
        CourseEntity.find { CourseTable.name eq name }.firstOrNull()

    fun findProfessionalFamilyById(id: UUID): ProfessionalFamilyEntity? =
        ProfessionalFamilyEntity.findById(id)

    fun save(request: CourseRequestDTO): CourseResponseDTO {
        if (findByName(request.name) != null) {
            throw CourseNameAlreadyExistsException()
        }
        val professionalFamily = findProfessionalFamilyById(UUID.fromString(request.professionalFamilyId))
            ?: throw IllegalArgumentException("Professional family not found")
        
        val entity = CourseEntity.new {
            name = request.name
            isEnabled = request.isEnabled
            this.professionalFamily = professionalFamily
        }
        return CourseResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            isEnabled = entity.isEnabled,
            professionalFamilyId = entity.professionalFamily.id.value.toString()
        )
    }

    fun update(id: UUID, request: CourseRequestDTO): CourseResponseDTO? {
        val entity = CourseEntity.findById(id) ?: return null
        val existingWithName = findByName(request.name)
        if (existingWithName != null && existingWithName.id.value != id) {
            throw CourseNameAlreadyExistsException()
        }
        val professionalFamily = findProfessionalFamilyById(UUID.fromString(request.professionalFamilyId))
            ?: throw IllegalArgumentException("Professional family not found")
        
        entity.name = request.name
        entity.isEnabled = request.isEnabled
        entity.professionalFamily = professionalFamily
        return CourseResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            isEnabled = entity.isEnabled,
            professionalFamilyId = entity.professionalFamily.id.value.toString()
        )
    }

    fun delete(id: UUID): Boolean {
        val entity = CourseEntity.findById(id) ?: return false
        entity.delete()
        return true
    }
}