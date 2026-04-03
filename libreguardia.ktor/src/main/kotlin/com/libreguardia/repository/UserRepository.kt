package com.libreguardia.repository

import com.libreguardia.db.UserEntity
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.db.UserTable
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.dto.entityToResponse
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.*


class UserRepository {
    fun getAll(): List<UserResponseDTO> = UserEntity.all().map(::entityToResponse)
    fun getByUUID(
        uuid: UUID
    ): UserResponseDTO? {
        return UserEntity.findById(uuid)?.let { entityToResponse(it) }
    }

    fun getEntityByUUID(
        uuid: UUID
    ): UserEntity? {
        return UserEntity.findById(uuid)
    }

    fun save(
        userCreateDTO: UserCreateDTO,
        userRoleEntity: UserRoleEntity,
        hashedPassword: String
    ) {
        UserEntity.new {
            this.name = userCreateDTO.name
            surname = userCreateDTO.surname
            email = userCreateDTO.email
            phoneNumber = userCreateDTO.phoneNumber
            password = hashedPassword
            isEnabled = userCreateDTO.isEnabled
            userRole = userRoleEntity
        }
    }

    fun editByUUID(
        uuid: UUID,
        userEditDTO: UserEditDTO,
        userRoleEntity: UserRoleEntity?,
    ): Boolean {
        return UserEntity.findByIdAndUpdate(uuid) { userEdit ->
            userEditDTO.name?.let { userEdit.name = it }
            userEditDTO.surname?.let { userEdit.surname = it }
            userEditDTO.email?.let { userEdit.email = it }
            userEditDTO.phoneNumber?.let { userEdit.phoneNumber = it }
            userEditDTO.newPassword?.let { userEdit.password = it }
            userEditDTO.isEnabled?.let { userEdit.isEnabled = it }
            userRoleEntity?.let { userEdit.userRole = it }
        } != null
    }

    fun deleteUser(
        uuid: UUID,
    ): Boolean {
        return UserTable.deleteWhere { UserTable.id eq uuid } == 1
    }

    fun softDeleteUser(
        uuid: UUID,
    ): Boolean {
        return UserTable.update({ UserTable.id eq uuid }) {
            it[isEnabled] = false
            it[isDeleted] = true
        } == 1
    }

    fun disableUser(
        uuid: UUID
    ): Boolean {
        return UserTable.update({ UserTable.id eq uuid }) {
            it[isEnabled] = false
        } == 1
    }
}