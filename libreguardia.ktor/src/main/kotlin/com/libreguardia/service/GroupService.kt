package com.libreguardia.service

import com.libreguardia.dto.module.GroupCreateDTO
import com.libreguardia.dto.module.GroupEditDTO
import com.libreguardia.exception.GroupNotFoundException
import com.libreguardia.frontend.component.main.create.GroupCreateField
import com.libreguardia.frontend.component.main.edit.GroupEditField
import com.libreguardia.model.GroupModel
import com.libreguardia.repository.CourseRepository
import com.libreguardia.repository.GroupRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
import java.util.*

class GroupService(
    private val groupRepository: GroupRepository,
    private val courseRepository: CourseRepository
) {
    suspend fun getAll(): List<GroupModel> = withTransaction { groupRepository.getAll() }

    suspend fun getThis(
        uuid: UUID
    ): GroupModel =
        withTransaction { groupRepository.getThis(uuid) } ?: throw GroupNotFoundException()

    suspend fun create(
        groupCreateDTO: GroupCreateDTO
    ): OperationResult {
        val errors = groupCreateDTO.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (groupRepository.alreadyExists(
                    code = groupCreateDTO.code,
                    courseId = groupCreateDTO.courseId
                )
            ) errors[GroupCreateField.CODE] = "This group already exists"
            if (!courseRepository.exists(
                    uuid = groupCreateDTO.courseId
                )
            ) errors[GroupCreateField.COURSE] = "This course doesn't exists"
            if (containsErrors(errors)) return@withTransaction OperationResult.Error(errors)
            groupRepository.save(
                dto = groupCreateDTO
            )
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun editThis(
        uuid: UUID,
        groupEditDTO: GroupEditDTO
    ): OperationResult {
        val errors = groupEditDTO.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        return withTransaction {
            if (groupRepository.alreadyExists(
                    uuid = uuid,
                    code = groupEditDTO.code,
                    courseId = groupEditDTO.courseId
                )
            ) errors[GroupEditField.CODE] = "This group already exists"
            if (!courseRepository.exists(
                    uuid = groupEditDTO.courseId
                )
            ) errors[GroupEditField.COURSE] = "This course doesn't exists"
            if (containsErrors(errors)) return@withTransaction OperationResult.Error(errors)
            if (!groupRepository.updateThis(
                    uuid = uuid,
                    groupEditDTO = groupEditDTO
                )
            ) throw GroupNotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun delete(
        uuid: UUID
    ) {
        withTransaction {
            if (!groupRepository.delete(uuid)) throw GroupNotFoundException()
        }
    }
}