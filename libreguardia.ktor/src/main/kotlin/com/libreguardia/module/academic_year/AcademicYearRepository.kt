package com.libreguardia.module.academic_year

import com.libreguardia.module.shared.repository.BaseRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class AcademicYearRepository : BaseRepository<AcademicYearTable>(AcademicYearTable) {
    fun getAll(): List<AcademicYearModel> = AcademicYearEntity
        .all()
        .map { it.toModel() }

    fun getByUUID(uuid: UUID): AcademicYearModel? =
        AcademicYearEntity.findById(uuid)?.toModel()

    fun save(dto: AcademicYearCreateDTO) {
        AcademicYearTable.insert {
            it[name] = dto.name
            it[startDate] = dto.startDate
            it[endDate] = dto.endDate
        }
    }

    fun update(
        uuid: UUID,
        dto: AcademicYearEditDTO
    ): Boolean =
        AcademicYearTable.update({ AcademicYearTable.id eq uuid }) { updated ->
            updated[name] = dto.name
            updated[startDate] = dto.startDate
            updated[endDate] = dto.endDate
        } == 1
}