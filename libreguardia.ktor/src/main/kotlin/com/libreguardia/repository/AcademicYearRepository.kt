package com.libreguardia.repository

import com.libreguardia.db.AcademicYearEntity
import com.libreguardia.db.AcademicYearTable
import com.libreguardia.dto.AcademicYearRequestDTO
import com.libreguardia.dto.AcademicYearResponseDTO
import kotlinx.datetime.LocalDate
import java.util.UUID

class AcademicYearRepository {
    fun all(): List<AcademicYearResponseDTO> =
        AcademicYearEntity.all().map { entity ->
            AcademicYearResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                startDate = entity.startDate,
                endDate = entity.endDate
            )
        }

    fun findById(id: UUID): AcademicYearResponseDTO? =
        AcademicYearEntity.findById(id)?.let { entity ->
            AcademicYearResponseDTO(
                id = entity.id.value.toString(),
                name = entity.name,
                startDate = entity.startDate,
                endDate = entity.endDate
            )
        }

    fun save(request: AcademicYearRequestDTO): AcademicYearResponseDTO {
        val entity = AcademicYearEntity.new {
            name = request.name
            startDate = request.startDate
            endDate = request.endDate
        }
        return AcademicYearResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            startDate = entity.startDate,
            endDate = entity.endDate
        )
    }

    fun update(id: UUID, request: AcademicYearRequestDTO): AcademicYearResponseDTO? {
        val entity = AcademicYearEntity.findById(id) ?: return null
        entity.name = request.name
        entity.startDate = request.startDate
        entity.endDate = request.endDate
        return AcademicYearResponseDTO(
            id = entity.id.value.toString(),
            name = entity.name,
            startDate = entity.startDate,
            endDate = entity.endDate
        )
    }

    fun delete(id: UUID): Boolean {
        val entity = AcademicYearEntity.findById(id) ?: return false
        entity.delete()
        return true
    }
}
