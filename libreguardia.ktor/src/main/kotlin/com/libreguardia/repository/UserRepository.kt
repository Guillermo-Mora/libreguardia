package com.libreguardia.repository

import com.libreguardia.db.Role
import com.libreguardia.db.model.GroupEntity
import com.libreguardia.db.model.PlaceEntity
import com.libreguardia.db.model.ScheduleEntity
import com.libreguardia.db.model.UserEntity
import com.libreguardia.db.model.UserTable
import com.libreguardia.dto.module.UserCreateDTO
import com.libreguardia.dto.module.UserEditDTO
import com.libreguardia.dto.module.UserEditProfileDTO
import com.libreguardia.model.UserModel
import com.libreguardia.model.UserProfileModel
import com.libreguardia.model.entityToProfileModel
import com.libreguardia.model.toModel
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import java.util.*

class UserRepository {
    fun getAll(): List<UserModel> =
        UserEntity
            .find { UserTable.isDeleted eq false }
            .map(UserEntity::toModel)

    fun getAllEnabled(): List<UserModel> =
        UserEntity
            .find { UserTable.isEnabled eq true }
            .map(UserEntity::toModel)

    fun getByUUID(
        uuid: UUID
    ): UserModel? {
        return UserEntity
            .findById(uuid)
            ?.toModel()
    }

    fun getProfileByUUID(
        uuid: UUID
    ): UserProfileModel? {
        return UserEntity.findById(uuid)
            ?.load(
                //Chained relations work perfectly with eager loading, already tested in the service transaction
                // layer by adding a logger and seeing the SQL queries executed.
                UserEntity::schedules,
                ScheduleEntity::group,
                ScheduleEntity::scheduleActivity,
                ScheduleEntity::place,
                GroupEntity::course,
                PlaceEntity::building,
                PlaceEntity::placeType,
            )
            ?.let { entityToProfileModel(it) }
    }

    fun getEntity(
        email: String
    ): UserEntity? {
        return UserEntity.find { UserTable.email eq email }.limit(1).firstOrNull()
    }

    fun getEntity(
        uuid: UUID
    ): UserEntity? {
        return UserEntity.findById(uuid)
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
            userUpdated[name] = userEditDTO.name
            userUpdated[surname] = userEditDTO.surname
            userUpdated[email] = userEditDTO.email
            userUpdated[phoneNumber] = userEditDTO.phoneNumber
            hashedPassword?.let { userUpdated[password] = it }
            userUpdated[isEnabled] = userEditDTO.isEnabled
            userUpdated[role] = Role.valueOf(userEditDTO.role)
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

    fun getPhoneNumber(
        userUuid: UUID
    ): String? =
        UserTable
            .select(UserTable.phoneNumber)
            .where { UserTable.id eq userUuid }
            .limit(1)
            .map { it[UserTable.phoneNumber] }
            .firstOrNull()

    fun isEmailTaken(
        uuid: UUID,
        email: String
    ): Boolean =
        UserTable
            .select(UserTable.email)
            .where { UserTable.id neq uuid and (UserTable.email eq email) }
            .limit(1)
            .count().toInt() >= 1

    fun isEmailTaken(
        email: String
    ): Boolean =
        UserTable
            .select(UserTable.email)
            .where { UserTable.email eq email }
            .limit(1)
            .count().toInt() >= 1
}