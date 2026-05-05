package com.libreguardia.repository

import com.libreguardia.db.model.CourseEntity
import com.libreguardia.db.model.CourseTable
import com.libreguardia.db.model.ProfessionalFamilyEntity
import com.libreguardia.dto.module.CourseCreateDTO
import com.libreguardia.dto.module.CourseEditDTO
import com.libreguardia.dto.module.CourseResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class CourseRepository {
    fun getAll(): List<CourseResponseDTO> = CourseEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: UUID): CourseResponseDTO? =
        CourseEntity.findById(uuid)?.toResponseDTO()

    fun professionalFamilyExists(professionalFamilyId: UUID): Boolean =
        ProfessionalFamilyEntity.findById(professionalFamilyId) != null

    fun save(dto: CourseCreateDTO) {
        CourseTable.insert {
            it[name] = dto.name
            it[professionalFamily] = dto.professionalFamilyId
        }
    }

    fun update(uuid: UUID, dto: CourseEditDTO): Boolean {
        return CourseTable.update({ CourseTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
        } == 1
    }

    fun delete(uuid: UUID): Boolean {
        return CourseTable.update({ CourseTable.id eq uuid }) {
            it[isEnabled] = false
        } == 1
    }

    fun toggleEnabled(uuid: UUID, enabled: Boolean): Boolean {
        return CourseTable.update({ CourseTable.id eq uuid }) {
            it[isEnabled] = enabled
        } == 1
    }
}