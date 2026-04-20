package com.libreguardia.repository

import com.libreguardia.db.model.ProfessionalFamilyEntity
import com.libreguardia.db.model.ProfessionalFamilyTable
import com.libreguardia.dto.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.ProfessionalFamilyEditDTO
import com.libreguardia.dto.ProfessionalFamilyResponseDTO
import com.libreguardia.dto.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ProfessionalFamilyRepository {
    fun getAll(): List<ProfessionalFamilyResponseDTO> = ProfessionalFamilyEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: UUID): ProfessionalFamilyResponseDTO? =
        ProfessionalFamilyEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: ProfessionalFamilyCreateDTO) {
        ProfessionalFamilyTable.insert {
            it[name] = dto.name
        }
    }

    fun update(uuid: UUID, dto: ProfessionalFamilyEditDTO): Boolean {
        return ProfessionalFamilyTable.update({ ProfessionalFamilyTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
        } == 1
    }

    fun delete(uuid: UUID): Boolean {
        return ProfessionalFamilyTable.update({ ProfessionalFamilyTable.id eq uuid }) {
            it[isEnabled] = false
        } == 1
    }

    fun toggleEnabled(uuid: UUID, enabled: Boolean): Boolean {
        return ProfessionalFamilyTable.update({ ProfessionalFamilyTable.id eq uuid }) {
            it[isEnabled] = enabled
        } == 1
    }
}