package com.libreguardia.service

import com.libreguardia.dto.module.GroupCreateDTO
import com.libreguardia.dto.module.GroupEditDTO
import com.libreguardia.dto.module.GroupResponseDTO
import com.libreguardia.exception.GroupNotFoundException
import com.libreguardia.repository.GroupRepository
import com.libreguardia.util.withTransaction
import java.util.UUID

class GroupService(
    private val repository: GroupRepository
) {
    suspend fun getAll(): List<GroupResponseDTO> = withTransaction { repository.getAll() }

    suspend fun getByUUID(uuid: UUID): GroupResponseDTO =
        withTransaction { repository.getByUUID(uuid) } ?: throw GroupNotFoundException()

    suspend fun create(dto: GroupCreateDTO) {
        withTransaction { repository.save(dto) }
    }

    suspend fun update(uuid: UUID, dto: GroupEditDTO) {
        withTransaction {
            if (!repository.update(uuid, dto)) throw GroupNotFoundException()
        }
    }

    suspend fun delete(uuid: UUID) {
        withTransaction {
            if (!repository.delete(uuid)) throw GroupNotFoundException()
        }
    }
}
