package com.libreguardia.repository

import com.libreguardia.db.model.ZoneEntity
import com.libreguardia.db.model.ZoneTable
import com.libreguardia.dto.module.ZoneCreateDTO
import com.libreguardia.dto.module.ZoneEditDTO
import com.libreguardia.dto.module.ZoneResponseDTO
import com.libreguardia.dto.module.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ZoneRepository {
    fun getAll(): List<ZoneResponseDTO> = ZoneEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: UUID): ZoneResponseDTO? =
        ZoneEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: ZoneCreateDTO) {
        ZoneTable.insert {
            it[name] = dto.name
        }
    }

    fun update(uuid: UUID, dto: ZoneEditDTO): Boolean {
        return ZoneTable.update({ ZoneTable.id eq uuid }) { updated ->
            dto.name?.let { updated[name] = it }
        } == 1
    }

    fun delete(uuid: UUID): Boolean {
        return ZoneTable.deleteWhere { ZoneTable.id eq uuid } == 1
    }
}