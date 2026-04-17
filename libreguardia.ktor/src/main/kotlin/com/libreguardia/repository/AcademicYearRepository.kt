package com.libreguardia.repository

import com.libreguardia.db.model.AcademicYearEntity
import com.libreguardia.db.model.AcademicYearTable
import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.dto.AcademicYearResponseDTO
import com.libreguardia.dto.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class AcademicYearRepository {
    fun getAll(): List<AcademicYearResponseDTO> = AcademicYearEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: UUID): AcademicYearResponseDTO? = AcademicYearEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: AcademicYearCreateDTO) {
        AcademicYearTable.insert {
            it[name] = dto.name
            it[startDate] = dto.startDate
            it[endDate] = dto.endDate
        }
    }

    fun update(uuid: UUID, dto: AcademicYearEditDTO): Boolean =
        AcademicYearTable.update({ AcademicYearTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
            dto.startDate?.let { updated[startDate] = it }
            dto.endDate?.let { updated[endDate] = it }
        } == 1

    fun delete(uuid: UUID): Boolean =
        AcademicYearTable.update({ AcademicYearTable.id eq uuid }) {
            it[isEnabled] = false
        } == 1

    fun toggleEnabled(uuid: UUID, enabled: Boolean): Boolean =
        AcademicYearTable.update({ AcademicYearTable.id eq uuid }) {
            it[isEnabled] = enabled
        } == 1
}
