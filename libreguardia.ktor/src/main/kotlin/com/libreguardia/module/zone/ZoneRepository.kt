package com.libreguardia.module.zone

import com.libreguardia.module.shared.repository.BaseRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ZoneRepository : BaseRepository<ZoneTable>(ZoneTable) {
    fun getAll(): List<ZoneModel> = ZoneEntity.all().map { it.toModel() }

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
}