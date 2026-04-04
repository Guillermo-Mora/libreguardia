package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.UserAlreadyDeletedException
import com.libreguardia.config.UserNotFoundException
import com.libreguardia.config.withTransaction
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.config.UserRoleNotFoundException
import com.libreguardia.config.IncorrectPasswordException
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.repository.UserRoleRepository
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.time.Clock

private const val BCRYPT_HASH_COST = 10

class UserService (
    private val userRepository: UserRepository,
    private val absenceRepository: AbsenceRepository,
    private val serviceRepository: ServiceRepository,
    private val scheduleRepository: ScheduleRepository,
    private val userRoleRepository: UserRoleRepository,
) {
    private val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
    private val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()


    //[Admin]
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

    //[Admin]
    suspend fun createUser(
        userCreateDTO: UserCreateDTO
    ) {
        val hashedPassword = bcryptHasher.hashToString(
            BCRYPT_HASH_COST,
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

    //[Admin]
    suspend fun editUser(
        userUUID: UUID,
        userEditDTO: UserEditDTO
    ) {
        withTransaction {
            if (userEditDTO.userRoleUUID != null) {
                if (!userRoleRepository.existsByUUID(userEditDTO.userRoleUUID)) throw UserRoleNotFoundException(
                    userEditDTO.userRoleUUID.toString()
                )
            }
            val hashedPassword = userEditDTO.newPassword?.let { newPassword ->
                bcryptHasher.hashToString(
                    BCRYPT_HASH_COST,
                    newPassword.toCharArray()
                )
            }
            if (!userRepository.editByUUID(
                    userUUID = userUUID,
                    userEditDTO = userEditDTO,
                    hashedPassword = hashedPassword
                )
            ) throw UserNotFoundException(userUUID.toString())
        }
    }

    //[Admin]
    suspend fun deleteUser(
        userUUID: UUID
    ) {
        withTransaction {
            val userEntity =
                userRepository.getEntityByUUID(userUUID) ?: throw UserNotFoundException(userUUID.toString())
            if (userEntity.isDeleted && !userEntity.isEnabled) throw UserAlreadyDeletedException(userUUID.toString())
            val dateTimeNow: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            scheduleRepository.deleteSchedulesByUserUUID(userUUID)
            serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                userUUID = userUUID,
                dateTimeNow = dateTimeNow
            )
            absenceRepository.deleteAbsencesByUserUUIDAfterNow(
                userUUID = userUUID,
                dateTimeNow = dateTimeNow
            )
            if (
                absenceRepository.existsAbsenceByUserUUIDPreviousToNow(
                    userUUID = userUUID,
                    dateTimeNow = dateTimeNow
                ) ||
                serviceRepository.existsServiceAssignedOrCoveredByUserUUIDPreviousToNow(
                    userUUID = userUUID,
                    dateTimeNow = dateTimeNow
                )
            ) {
                if (!userRepository.softDeleteUser(userUUID)) throw UserNotFoundException(userUUID.toString())
            } else if (!userRepository.deleteUser(userUUID)) throw UserNotFoundException(userUUID.toString())
        }
    }

    //[Admin]
    suspend fun toggleEnableUser(
        userUUID: UUID,
        enableOrDisable: Boolean
    ) {
        withTransaction {
            if (!userRepository.toggleEnableUser(
                    uuid = userUUID,
                    enableOrDisable = enableOrDisable
                )
            ) throw UserNotFoundException(userUUID.toString())
        }
    }

    suspend fun editUserProfile(
        userUUID: UUID,
        userEditProfileDTO: UserEditProfileDTO
    ) {
        withTransaction {
            var hashedPassword: String? = null
            if (userEditProfileDTO.currentPassword != null && userEditProfileDTO.newPassword != null) {
                val hashedCurrentPassword =
                    userRepository.getHashedPassword(userUUID) ?: throw UserNotFoundException(userUUID.toString())
                val verificationResult = bcryptVerifyer.verify(
                    userEditProfileDTO.currentPassword.toByteArray(),
                    hashedCurrentPassword.toByteArray()
                )
                if (!verificationResult.verified || !verificationResult.validFormat)
                    throw IncorrectPasswordException()
                hashedPassword =
                    bcryptHasher.hashToString(
                        BCRYPT_HASH_COST,
                        userEditProfileDTO.newPassword.toCharArray()
                    )
            }
            if (!userRepository.editUserProfileByUUID(
                    userUUID = userUUID,
                    userEditProfileDTO = userEditProfileDTO,
                    hashedPassword = hashedPassword
                )
            ) throw UserNotFoundException(userUUID.toString())
        }
    }
}