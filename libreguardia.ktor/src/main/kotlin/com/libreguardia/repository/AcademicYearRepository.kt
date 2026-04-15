package com.libreguardia.repository

import com.libreguardia.db.model.AcademicYearEntity
import com.libreguardia.db.model.AcademicYearTable
import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.dto.AcademicYearResponseDTO
import com.libreguardia.dto.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID

class AcademicYearRepository {
    fun getAll(): List<AcademicYearResponseDTO> = transaction {
        AcademicYearEntity.all().map { it.toResponseDTO() }
    }

    fun getByUUID(uuid: UUID): AcademicYearResponseDTO? = transaction {
        AcademicYearEntity.findById(uuid)?.toResponseDTO()
    }

    fun save(dto: AcademicYearCreateDTO): AcademicYearResponseDTO = transaction {
        AcademicYearEntity.new {
            name = dto.name
            startDate = dto.startDate
            endDate = dto.endDate
        }.toResponseDTO()
    }

    fun update(uuid: UUID, dto: AcademicYearEditDTO): Boolean = transaction {
        val entity = AcademicYearEntity.findById(uuid) ?: return@transaction false
        dto.name?.let { entity.name = it }
        dto.startDate?.let { entity.startDate = it }
        dto.endDate?.let { entity.endDate = it }
        true
    }

    fun delete(uuid: UUID): Boolean = transaction {
        val entity = AcademicYearEntity.findById(uuid) ?: return@transaction false
        entity.isEnabled = false
        true
    }

    fun toggleEnabled(uuid: UUID, enabled: Boolean): Boolean = transaction {
        val entity = AcademicYearEntity.findById(uuid) ?: return@transaction false
        entity.isEnabled = enabled
        true
    }
}
