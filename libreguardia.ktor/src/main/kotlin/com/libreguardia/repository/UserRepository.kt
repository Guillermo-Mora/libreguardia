package com.libreguardia.repository

import com.libreguardia.db.UserEntity
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.dto.entityToResponse
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
        userEntity: UserEntity
    ) {
        userEntity.delete()
    }
}