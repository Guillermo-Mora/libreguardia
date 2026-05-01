package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.BCRYPT_HASH_COST
import com.libreguardia.dto.EditUserProfileResult
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.dto.validate
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.model.UserModel
import com.libreguardia.model.UserProfileModel
import com.libreguardia.repository.*
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.validateNewPassword
import com.libreguardia.validation.validatePhoneNumber
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
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
        withTransaction {
            //Temporary for testing
            addLogger(StdOutSqlLogger)
            //
            val userEntity = userRepository.getEntity(uuid = userUuid) ?: throw UserNotFoundException()
            val userWeeklySchedules = scheduleRepository.getUserWeeklySchedules(userUUID = userUuid)
            UserProfileModel(
                fullName = "${userEntity.name} ${userEntity.surname}",
                email = userEntity.email,
                phoneNumber = userEntity.phoneNumber,
                role = userEntity.role.toString(),
                schedules = userWeeklySchedules
            )
        }
    //Eager loading entities works perfectly. References of references, of references, etc. Also work,
    // as the log shows there's no N+1 problem, but still too much queries in my opinion :(
    //userRepository.getProfileByUUID(userUuid) ?: throw UserNotFoundException() }

    suspend fun createUser(
        userCreateDTO: UserCreateDTO
    ): OperationResult {
        val errors: List<String?> = userCreateDTO.validate()
        if (errors.any { it != null }) return OperationResult.Error(errors)
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
        return OperationResult.Success()
    }

    suspend fun editUser(
        userUuid: UUID,
        userEditDTO: UserEditDTO
    ): OperationResult {
        val errors: List<String?> = userEditDTO.validate()
        if (errors.any { it != null }) return OperationResult.Error(errors)
        val dateTimeNow: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val hashedPassword = if (!userEditDTO.password.isNullOrBlank()) {
            bcryptHasher.hashToString(
                BCRYPT_HASH_COST,
                userEditDTO.password.toCharArray()
            )
        } else null
        userEditDTO.isEnabled == false || userEditDTO.role != null
        withTransaction {
            //It would be better to only get the role, using Exposed DSL, but for now it stays like this.
            val user = userRepository.getEntity(uuid = userUuid)
            val shouldCloseSessions = hashedPassword != null || userEditDTO.role != user?.role.toString()
            //
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
        return OperationResult.Success()
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
        sessionUuid: UUID,
        userEditProfileDTO: UserEditProfileDTO
    ): EditUserProfileResult {
        val phoneNumberError = validatePhoneNumber(userEditProfileDTO.phoneNumber, true)
        var currentPasswordError: String? = "Incorrect password"
        var newPasswordError = validateNewPassword(
            field = userEditProfileDTO.newPassword,
            required = false
        )
        return withTransaction {
            //Temporary for testing
            addLogger(StdOutSqlLogger)
            //
            var hashedPassword: String? = null
            if (!userEditProfileDTO.currentPassword.isNullOrEmpty()) {
                if (userEditProfileDTO.newPassword.isNullOrEmpty()) {
                    newPasswordError = "New password is empty"
                } else {
                    val hashedCurrentPassword =
                        userRepository.getHashedPassword(userUuid) ?: throw UserNotFoundException()
                    val verificationResult = bcryptVerifyer.verify(
                        userEditProfileDTO.currentPassword.toByteArray(),
                        hashedCurrentPassword.toByteArray()
                    )
                    currentPasswordError = if (!verificationResult.verified) "Incorrect password" else null
                    hashedPassword =
                        bcryptHasher.hashToString(
                            BCRYPT_HASH_COST,
                            userEditProfileDTO.newPassword.toCharArray()
                        )
                }
            } else currentPasswordError = null
            if (!userEditProfileDTO.newPassword.isNullOrEmpty() && userEditProfileDTO.currentPassword.isNullOrEmpty())
                currentPasswordError = "Current password empty"
            if (phoneNumberError != null ||
                currentPasswordError != null ||
                newPasswordError != null
            ) {
                return@withTransaction EditUserProfileResult.Error(
                    userEditProfileDTO = userEditProfileDTO,
                    phoneNumberError = phoneNumberError,
                    currentPasswordError = currentPasswordError,
                    newPasswordError = newPasswordError
                )
            }
            if (!userRepository.editUserProfileByUUID(
                    userUUID = userUuid,
                    userEditProfileDTO = userEditProfileDTO,
                    hashedPassword = hashedPassword
                )
            ) throw UserNotFoundException()
            sessionRepository.deleteOtherSessionsFromUser(
                sessionUuid = sessionUuid,
                userUuid = userUuid,
            )
            EditUserProfileResult.Success(
                userPhoneNumber = userEditProfileDTO.phoneNumber.toString()
            )
        }
    }

    suspend fun getUserPhoneNumber(
        userUuid: UUID
    ) =
        withTransaction { userRepository.getPhoneNumber(userUuid) ?: throw UserNotFoundException() }

}