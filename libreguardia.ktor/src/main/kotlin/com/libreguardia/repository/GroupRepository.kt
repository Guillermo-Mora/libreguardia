package com.libreguardia.repository

import com.libreguardia.db.model.GroupEntity
import com.libreguardia.db.model.GroupTable
import com.libreguardia.dto.GroupCreateDTO
import com.libreguardia.dto.GroupEditDTO
import com.libreguardia.dto.GroupResponseDTO
import com.libreguardia.dto.toResponseDTO
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class GroupRepository {
    fun getAll(): List<GroupResponseDTO> = GroupEntity.all().map { it.toResponseDTO() }

    fun getByUUID(uuid: UUID): GroupResponseDTO? =
        GroupEntity.findById(uuid)?.toResponseDTO()

    fun save(dto: GroupCreateDTO) {
        GroupTable.insert {
            it[code] = dto.code
            dto.pointsMultiplier?.let { multiplier -> it[pointsMultiplier] = multiplier.toBigDecimal() }
            it[course] = dto.courseId
        }
    }

    fun update(uuid: UUID, dto: GroupEditDTO): Boolean {
        return GroupTable.update({ GroupTable.id eq uuid }) { updated ->
            dto.code?.let { updated[code] = it }
            dto.pointsMultiplier?.let { multiplier -> updated[pointsMultiplier] = multiplier.toBigDecimal() }
            dto.courseId?.let { updated[course] = it }
        } == 1
    }

    fun delete(uuid: UUID): Boolean {
        return GroupTable.update({ GroupTable.id eq uuid }) {
            it[isEnabled] = false
        } == 1
    }

    fun toggleEnabled(uuid: UUID, enabled: Boolean): Boolean {
        return GroupTable.update({ GroupTable.id eq uuid }) {
            it[isEnabled] = enabled
        } == 1
    }
}
