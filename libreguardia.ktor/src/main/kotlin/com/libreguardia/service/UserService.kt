package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.BCRYPT_HASH_COST
import com.libreguardia.db.model.UserRoleEntity
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.exception.IncorrectPasswordException
import com.libreguardia.exception.UserAlreadyDeletedException
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.exception.UserRoleNotFoundException
import com.libreguardia.repository.*
import com.libreguardia.utils.withTransaction
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*
import kotlin.time.Clock

class UserService (
    private val bcryptVerifyer: BCrypt.Verifyer,
    private val bcryptHasher: BCrypt.Hasher,
    private val clock: Clock.System,
    private val userRepository: UserRepository,
    private val absenceRepository: AbsenceRepository,
    private val serviceRepository: ServiceRepository,
    private val scheduleRepository: ScheduleRepository,
    private val userRoleRepository: UserRoleRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    //[Admin]
    suspend fun getAllUsers(): List<UserResponseDTO> = withTransaction {
        userRepository.getAll()
    }

    suspend fun getUser(
        userUUID: UUID
    ): UserResponseDTO {
        return withTransaction {
            val user = userRepository.getByUUID(userUUID) ?: throw UserNotFoundException()
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
            if (!userRoleRepository.existsByUUID(userCreateDTO.userRoleUUID)) throw UserRoleNotFoundException()
            userRepository.save(
                userCreateDTO = userCreateDTO,
                hashedPassword = hashedPassword
            )
        }
    }

    //[Admin]
    suspend fun editUser(
        userUUID: UUID,
        userEditDTO: UserEditDTO
    ) {
        val dateTimeNow: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
        withTransaction {
            if (userEditDTO.userRoleUUID != null) {
                if (!userRoleRepository.existsByUUID(userEditDTO.userRoleUUID)) throw UserRoleNotFoundException()
            }
            val hashedPassword = userEditDTO.password?.let { newPassword ->
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
            ) throw UserNotFoundException()
            userEditDTO.isEnabled?.let {
                if (!it) {
                    serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                        userUUID = userUUID,
                        dateTimeNow = dateTimeNow
                    )
                    refreshTokenRepository.deleteRefreshTokensByUser(userUUID)
                }
            }
        }
    }

    //[Admin]
    suspend fun deleteUser(
        userUUID: UUID
    ) {
        withTransaction {
            val userEntity =
                userRepository.getEntity(userUUID) ?: throw UserNotFoundException()
            if (userEntity.isDeleted && !userEntity.isEnabled) throw UserAlreadyDeletedException()
            val dateTimeNow: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
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
                serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                    userUUID = userUUID,
                    dateTimeNow = dateTimeNow
                )
                absenceRepository.deleteAbsencesByUserUUIDAfterNow(
                    userUUID = userUUID,
                    dateTimeNow = dateTimeNow
                )
                scheduleRepository.deleteSchedulesByUserUUID(userUUID)
                refreshTokenRepository.deleteRefreshTokensByUser(userUUID)
                if (!userRepository.softDeleteUser(userUUID)) throw UserNotFoundException()
            } else if (!userRepository.deleteUser(userUUID)) throw UserNotFoundException()
        }
    }

    //[Admin]
    suspend fun toggleEnableUser(
        userUUID: UUID,
        enableOrDisable: Boolean
    ) {
        val dateTimeNow: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
        withTransaction {
            if (!userRepository.toggleEnableUser(
                    uuid = userUUID,
                    enableOrDisable = enableOrDisable
                )
            ) throw UserNotFoundException()
            if (!enableOrDisable) {
                serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                    userUUID = userUUID,
                    dateTimeNow = dateTimeNow
                )
                refreshTokenRepository.deleteRefreshTokensByUser(userUUID)
            }
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
                    userRepository.getHashedPassword(userUUID) ?: throw UserNotFoundException()
                val verificationResult = bcryptVerifyer.verify(
                    userEditProfileDTO.currentPassword.toByteArray(),
                    hashedCurrentPassword.toByteArray()
                )
                if (!verificationResult.verified) throw IncorrectPasswordException()
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
            ) throw UserNotFoundException()
        }
    }
}