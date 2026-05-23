package com.libreguardia.module.schedule_activity

import com.libreguardia.config.repository.BaseRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ScheduleActivityRepository : BaseRepository<ScheduleActivityTable>(ScheduleActivityTable) {
    fun getAll(): List<ScheduleActivityResponseDTO> =
        ScheduleActivityEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: UUID): ScheduleActivityResponseDTO? =
        ScheduleActivityEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: ScheduleActivityCreateDTO) {
        ScheduleActivityTable.insert {
            it[name] = dto.name
            it[generatesService] = dto.generatesService
        }
    }

    fun update(uuid: UUID, dto: ScheduleActivityEditDTO): Boolean {
        return ScheduleActivityTable.update({ ScheduleActivityTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
            dto.generatesService?.let { updated[generatesService] = it }
        } == 1
    }
}