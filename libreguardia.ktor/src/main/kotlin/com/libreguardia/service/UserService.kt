package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.BCRYPT_HASH_COST
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.exception.IncorrectPasswordException
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
    private val sessionRepository: SessionRepository
) {
    suspend fun getAllUsers(): List<UserModel> =
        withTransaction { userRepository.getAll() }

    suspend fun getUser(
        userUuid: UUID
    ) =
        withTransaction { userRepository.getByUUID(userUuid) ?: throw UserNotFoundException() }

    suspend fun getUserProfile(
        userUuid: UUID
    ) =
        withTransaction { userRepository.getProfileByUUID(userUuid) ?: throw UserNotFoundException() }

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
        userUuid: UUID,
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
                    userUUID = userUuid,
                    userEditDTO = userEditDTO,
                    hashedPassword = hashedPassword
                )
            ) throw UserNotFoundException()
            if (userEditDTO.isEnabled == false) {
                serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                    userUUID = userUuid,
                    dateTimeNow = dateTimeNow
                )
            }
            if (shouldCloseSessions) sessionRepository.deleteSessionsFromUser(userUuid = userUuid)
        }
    }

    suspend fun deleteUser(
        userUuid: UUID
    ) {
        withTransaction {
            val dateTimeNow: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
            if (
                absenceRepository.existsAbsenceByUserUUIDPreviousToNow(
                    userUUID = userUuid,
                    dateTimeNow = dateTimeNow
                ) ||
                serviceRepository.existsServiceAssignedOrCoveredByUserUUIDPreviousToNow(
                    userUUID = userUuid,
                    dateTimeNow = dateTimeNow
                )
            ) {
                serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                    userUUID = userUuid,
                    dateTimeNow = dateTimeNow
                )
                absenceRepository.deleteAbsencesByUserUUIDAfterNow(
                    userUUID = userUuid,
                    dateTimeNow = dateTimeNow
                )
                scheduleRepository.deleteSchedulesByUserUUID(userUuid)
                sessionRepository.deleteSessionsFromUser(userUuid = userUuid)
                if (!userRepository.softDeleteUser(userUuid)) throw UserNotFoundException()
            } else if (!userRepository.deleteUser(userUuid)) throw UserNotFoundException()
        }
    }

    suspend fun toggleEnableUser(
        userUuid: UUID,
        enableOrDisable: Boolean
    ) {
        val dateTimeNow: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
        withTransaction {
            if (!userRepository.toggleEnableUser(
                    uuid = userUuid,
                    enableOrDisable = enableOrDisable
                )
            ) throw UserNotFoundException()
            if (!enableOrDisable) {
                serviceRepository.setNullAssignedServicesToUserUUIDAfterNow(
                    userUUID = userUuid,
                    dateTimeNow = dateTimeNow
                )
                sessionRepository.deleteSessionsFromUser(userUuid = userUuid)
            }
        }
    }

    suspend fun editUserProfile(
        userUuid: UUID,
        userEditProfileDTO: UserEditProfileDTO
    ) {
        withTransaction {
            var hashedPassword: String? = null
            if (userEditProfileDTO.currentPassword != null && userEditProfileDTO.newPassword != null) {
                val hashedCurrentPassword =
                    userRepository.getHashedPassword(userUuid) ?: throw UserNotFoundException()
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
                    userUUID = userUuid,
                    userEditProfileDTO = userEditProfileDTO,
                    hashedPassword = hashedPassword
                )
            ) throw UserNotFoundException()
        }
    }
}