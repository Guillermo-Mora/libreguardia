package com.libreguardia.repository

import com.libreguardia.db.Role
import com.libreguardia.db.model.UserEntity
import com.libreguardia.db.model.UserTable
import com.libreguardia.dto.*
import com.libreguardia.model.UserModel
import com.libreguardia.model.UserProfileModel
import com.libreguardia.model.entityToModel
import com.libreguardia.model.entityToProfileModel
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import java.util.*

class UserRepository {
    fun getAll(): List<UserModel> = UserEntity.all().map(::entityToModel)
    fun getByUUID(
        uuid: UUID
    ): UserModel? {
        return UserEntity.findById(uuid)?.let { entityToModel(it) }
    }

    fun getProfileByUUID(
        uuid: UUID
    ): UserProfileModel? {
        return UserEntity.findById(uuid)?.let { entityToProfileModel(it) }
    }


    fun getEntity(
        email: String
    ): UserEntity? {
        return UserEntity.find { UserTable.email eq email }.limit(1).firstOrNull()
    }

    fun save(
        userCreateDTO: UserCreateDTO,
        hashedPassword: String
    ) {
        UserTable.insert {
            it[name] = userCreateDTO.name
            it[surname] = userCreateDTO.surname
            it[email] = userCreateDTO.email
            it[phoneNumber] = userCreateDTO.phoneNumber
            it[password] = hashedPassword
            it[isEnabled] = userCreateDTO.isEnabled
            it[role] = Role.valueOf(userCreateDTO.role)
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
            userEditDTO.role?.let { userUpdated[role] = Role.valueOf(it) }
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
            .where { UserTable.id eq userUUID }
            .limit(1)
            .map { it[UserTable.password] }
            .firstOrNull()
    }

    fun getUserUuid(
        email: String
    ): UUID? {
        return UserTable
            .select(UserTable.id)
            .where { UserTable.email eq email }
            .limit(1)
            .map { it[UserTable.id].value }
            .firstOrNull()
    }
}