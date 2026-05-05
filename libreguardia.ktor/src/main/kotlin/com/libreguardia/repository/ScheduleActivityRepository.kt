package com.libreguardia.repository

import com.libreguardia.db.model.ScheduleActivityEntity
import com.libreguardia.db.model.ScheduleActivityTable
import com.libreguardia.dto.module.ScheduleActivityCreateDTO
import com.libreguardia.dto.module.ScheduleActivityEditDTO
import com.libreguardia.dto.module.ScheduleActivityResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ScheduleActivityRepository {
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

    fun delete(uuid: UUID): Boolean {
        return ScheduleActivityTable.update({ ScheduleActivityTable.id eq uuid }) {
            it[isEnabled] = false
        } == 1
    }

    fun toggleEnabled(uuid: UUID, enabled: Boolean): Boolean {
        return ScheduleActivityTable.update({ ScheduleActivityTable.id eq uuid }) {
            it[isEnabled] = enabled
        } == 1
    }
}

