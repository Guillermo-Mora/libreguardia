package com.libreguardia.repository

import com.libreguardia.db.model.AcademicYearEntity
import com.libreguardia.db.model.AcademicYearTable
import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.dto.module.AcademicYearResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class AcademicYearRepository : BaseRepository<AcademicYearTable>(AcademicYearTable) {
    fun getAll(): List<AcademicYearResponseDTO> = AcademicYearEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: UUID): AcademicYearResponseDTO? =
        AcademicYearEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: AcademicYearCreateDTO) {
        AcademicYearTable.insert {
            it[name] = dto.name
            it[startDate] = dto.startDate
            it[endDate] = dto.endDate
        }
    }

    fun update(uuid: UUID, dto: AcademicYearEditDTO): Boolean {
        return AcademicYearTable.update({ AcademicYearTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
            dto.startDate?.let { updated[startDate] = it }
            dto.endDate?.let { updated[endDate] = it }
        } == 1
    }
}