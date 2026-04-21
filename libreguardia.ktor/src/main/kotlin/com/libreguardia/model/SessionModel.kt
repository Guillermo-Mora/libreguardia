package com.libreguardia.model

import com.libreguardia.db.Role
import com.libreguardia.repository.SessionRepository
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