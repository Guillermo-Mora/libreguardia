package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.BCRYPT_HASH_COST
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.exception.IncorrectPasswordException
import com.libreguardia.exception.UserAlreadyDeletedException
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.model.UserModel
import com.libreguardia.repository.*
import com.libreguardia.util.withTransaction
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
    private val refreshTokenRepository: RefreshTokenRepository
) {
    suspend fun getAllUsers(): List<UserModel> =
        withTransaction { userRepository.getAll() }

    suspend fun getUser(
        userUUID: UUID
    ) =
        withTransaction { userRepository.getByUUID(userUUID) ?: throw UserNotFoundException() }

    suspend fun createUser(
        userCreateDTO: UserCreateDTO
    ) {
        val hashedPassword = bcryptHasher.hashToString(
            BCRYPT_HASH_COST,
            userCreateDTO.password.toCharArray()
        )
        withTransaction {
            userRepository.save(
                userCreateDTO = userCreateDTO,
                hashedPassword = hashedPassword
            )
        }
    }

    suspend fun editUser(
        userUUID: UUID,
        userEditDTO: UserEditDTO
    ) {
        val dateTimeNow: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val hashedPassword = userEditDTO.password?.let { newPassword ->
            bcryptHasher.hashToString(
                BCRYPT_HASH_COST,
                newPassword.toCharArray()
            )
        }
        val shouldCloseSessions = userEditDTO.isEnabled == false || userEditDTO.role != null
        withTransaction {
            if (!userRepository.editByUUID(
                    userUUID = userUUID,
                    userEditDTO = userEditDTO,
                    hashedPassword = hashedPassword
                )
            ) throw UserNotFoundException()
            if (userEditDTO.isEnabled == false) {
                serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                    userUUID = userUUID,
                    dateTimeNow = dateTimeNow
                )
            }
            if (shouldCloseSessions) refreshTokenRepository.deleteRefreshTokensByUser(userUUID = userUUID)
        }
    }

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