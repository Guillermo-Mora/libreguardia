package com.libreguardia.repository

import com.libreguardia.db.model.GroupEntity
import com.libreguardia.db.model.GroupTable
import com.libreguardia.dto.module.GroupCreateDTO
import com.libreguardia.dto.module.GroupEditDTO
import com.libreguardia.model.GroupModel
import com.libreguardia.model.toModel
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class GroupRepository : BaseRepository<GroupTable>(GroupTable) {
    fun getAll(): List<GroupModel> = GroupEntity.all().map { it.toModel() }

    fun getThis(
        uuid: UUID
    ): GroupModel? =
        GroupEntity
            .findById(uuid)
            ?.toModel()

    fun save(dto: GroupCreateDTO) {
        GroupTable.insert {
            it[code] = dto.code
            it[pointsMultiplier] = dto.pointsMultiplier.toBigDecimal()
            it[course] = UUID.fromString(dto.courseId)
        }
    }

    fun updateThis(
        uuid: UUID,
        groupEditDTO: GroupEditDTO
    ): Boolean {
        return GroupTable.update({ GroupTable.id eq uuid }) {
            it[code] = groupEditDTO.code
            it[pointsMultiplier] = groupEditDTO.pointsMultiplier.toBigDecimal()
            it[course] = UUID.fromString(groupEditDTO.courseId)
        } == 1
    }

    fun exists(
        uuid: UUID,
        code: String,
        courseId: UUID
    ): Boolean =
        GroupTable
            .select(GroupTable.id)
            .where {
                GroupTable.id neq uuid and
                        (GroupTable.code eq code) and
                        (GroupTable.course eq courseId)
            }
            .limit(1)
            .count().toInt() >= 1

    fun exists(
        code: String,
        courseId: UUID
    ): Boolean =
        GroupTable
            .select(GroupTable.id)
            .where {
                GroupTable.code eq code and
                        (GroupTable.course eq courseId)
            }
            .limit(1)
            .count() >= 1
}