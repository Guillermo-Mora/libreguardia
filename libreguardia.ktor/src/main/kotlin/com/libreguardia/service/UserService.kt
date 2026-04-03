package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.UserAlreadyDeletedException
import com.libreguardia.config.UserNotFoundException
import com.libreguardia.config.withTransaction
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.config.UserRoleNotFoundException
import com.libreguardia.db.UserEntity
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.UserRepository
import java.util.UUID

class UserService (
    private val userRepository: UserRepository,
    private val absenceRepository: AbsenceRepository,
    private val serviceRepository: ServiceRepository,
    private val scheduleRepository: ScheduleRepository
) {
    suspend fun getAllUsers(): List<UserResponseDTO> = withTransaction {
        userRepository.getAll()
    }

    suspend fun getUser(
        userUUID: UUID
    ): UserResponseDTO {
        return withTransaction {
            val user = userRepository.getByUUID(userUUID) ?: throw UserNotFoundException(userUUID.toString())
            user
        }
    }

    suspend fun createUser(
        userCreateDTO: UserCreateDTO
    ) {
        val bcryptHasher = BCrypt.withDefaults()
        val hashedPassword = bcryptHasher.hashToString(
            10,
            userCreateDTO.password.toCharArray()
        )
        withTransaction {
            val userRoleEntity = UserRoleEntity.findById(userCreateDTO.userRoleUUID) ?: throw UserRoleNotFoundException(
                userCreateDTO.userRoleUUID.toString()
            )
            userRepository.save(
                userCreateDTO = userCreateDTO,
                userRoleEntity = userRoleEntity,
                hashedPassword = hashedPassword
            )
        }
    }

    suspend fun editUser(
        userUUID: UUID,
        userEditDTO: UserEditDTO
    ) {
        withTransaction {
            var userRoleEntity: UserRoleEntity? = null
            if (userEditDTO.userRoleUUID != null) {
                userRoleEntity = UserRoleEntity.findById(userEditDTO.userRoleUUID) ?: throw UserRoleNotFoundException(
                    userEditDTO.userRoleUUID.toString()
                )
            }
            //New password verification if the current and new password are passed, still to implement
            if (!userRepository.editByUUID(
                    uuid = userUUID,
                    userEditDTO = userEditDTO,
                    userRoleEntity = userRoleEntity
                )
            ) throw UserNotFoundException(userUUID.toString())
        }
    }

    suspend fun deleteUser(
        userUUID: UUID
    ) {
        withTransaction {
            val userEntity =
                userRepository.getEntityByUUID(userUUID) ?: throw UserNotFoundException(userUUID.toString())
            if (userEntity.isDeleted && !userEntity.isEnabled) throw UserAlreadyDeletedException(userUUID.toString())
            if (
                absenceRepository.existsAbsenceByUserUUID(userEntity.id.value) ||
                serviceRepository.existsServiceCoveredByUserUUID(userEntity.id.value) ||
                serviceRepository.existsServiceAssignedToUserUUIDPreviousToNow(userEntity.id.value)
            ) {
                userEntity.isEnabled = false
                userEntity.isDeleted = true
            } else {
                scheduleRepository.deleteSchedulesByUserUUID(userEntity.id.value)
                userRepository.deleteUser(userEntity)
            }
        }
    }

    suspend fun disableUser(
        userUUID: UUID
    ) {
        withTransaction {
            val userEntity =
                userRepository.getEntityByUUID(userUUID) ?: throw UserNotFoundException(userUUID.toString())
            userEntity.isEnabled = false
        }
    }
}