package com.libreguardia.repository

import com.libreguardia.db.model.AcademicYearEntity
import com.libreguardia.db.model.AcademicYearTable
import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.dto.module.AcademicYearResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import com.libreguardia.model.AcademicYearModel
import com.libreguardia.model.toModel
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class AcademicYearRepository : BaseRepository<AcademicYearTable>(AcademicYearTable) {
    fun getAll(): List<AcademicYearModel> = AcademicYearEntity.all().map { it.toModel() }

    fun getByUUID(uuid: UUID): AcademicYearModel? =
        AcademicYearEntity.findById(uuid)?.toModel()

    fun save(dto: AcademicYearCreateDTO) {
        AcademicYearTable.insert {
            it[name] = dto.name
            it[startDate] = dto.startDate
            it[endDate] = dto.endDate
        }
    }

    fun update(uuid: UUID, dto: AcademicYearEditDTO): Boolean {
        return AcademicYearTable.update({ AcademicYearTable.id eq uuid }) { updated ->
            updated[name] = dto.name
            updated[startDate] = LocalDate.parse(dto.startDate)
            updated[endDate] = LocalDate.parse(dto.endDate)
        } == 1
    }
}