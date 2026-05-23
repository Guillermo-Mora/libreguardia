package com.libreguardia.module.auth

import com.libreguardia.config.database.Role
import java.util.*
import kotlin.time.Instant

data class SessionModel(
    val expiresAt: Instant,
    val isEnabled: Boolean,
    val isDeleted: Boolean,
    val userUuid: UUID,
    val userRole: Role
)

fun SessionRepository.dataToModel(
    expiresAt: Instant,
    isEnabled: Boolean,
    isDeleted: Boolean,
    userUuid: UUID,
    userRole: Role
) {

}