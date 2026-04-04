package com.libreguardia.repository

import com.libreguardia.db.UserEntity
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.db.UserTable
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.dto.entityToResponse
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
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
        userUUID: UUID,
        userEditDTO: UserEditDTO,
        hashedPassword: String?
    ): Boolean {
        return UserTable.update({ UserTable.id eq userUUID }) { userUpdated ->
            userEditDTO.name?.let { userUpdated[name] = it }
            userEditDTO.surname?.let { userUpdated[surname] = it }
            userEditDTO.email?.let { userUpdated[email] = it }
            userEditDTO.phoneNumber?.let { userUpdated[phoneNumber] = it }
            hashedPassword?.let { userUpdated[password] = it }
            userEditDTO.isEnabled?.let { userUpdated[isEnabled] = it }
            userEditDTO.userRoleUUID?.let { userUpdated[userRole] = it }
        } == 1
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

    fun toggleEnableUser(
        uuid: UUID,
        enableOrDisable: Boolean
    ): Boolean {
        return UserTable.update({ UserTable.id eq uuid }) {
            it[isEnabled] = enableOrDisable
        } == 1
    }

    fun userExists(
        uuid: UUID
    ): Boolean = UserTable.select(UserTable.id eq uuid).limit(1).any()

    fun editUserProfileByUUID(
        userUUID: UUID,
        userEditProfileDTO: UserEditProfileDTO,
        hashedPassword: String?
    ): Boolean {
        return UserTable.update({ UserTable.id eq userUUID }) { userUpdated ->
            userEditProfileDTO.phoneNumber?.let { userUpdated[phoneNumber] = it }
            hashedPassword?.let { userUpdated[password] = it }
        } == 1
    }

    fun getHashedPassword(
        userUUID: UUID
    ): String? {
        return UserTable
            .select(UserTable.password)
            .where {
                UserTable.id eq userUUID
            }.limit(1)
            .firstOrNull()?.get(UserTable.password)
    }
}