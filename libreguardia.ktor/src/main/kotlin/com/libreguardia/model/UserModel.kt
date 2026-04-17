package com.libreguardia.model

import com.libreguardia.db.model.UserEntity
import com.libreguardia.repository.UserRepository
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

data class UserModel(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val isEnabled: Boolean,
    val isDeleted: Boolean,
    val role: String
)

fun UserRepository.entityToModel(
    entity: UserEntity
) = UserModel(
    id = entity.id.value,
    name = entity.name,
    surname = entity.surname,
    email = entity.email,
    phoneNumber = entity.phoneNumber,
    isEnabled = entity.isEnabled,
    isDeleted = entity.isDeleted,
    role = entity.role.toString()
)

data class UserProfileModel(
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val role: String
    //SCHEDULE HERE STILL TO IMPLEMENT
)

fun UserRepository.entityToProfileModel(
    entity: UserEntity
) = UserProfileModel(
    name = entity.name,
    surname = entity.surname,
    email = entity.email,
    phoneNumber = entity.phoneNumber,
    role = entity.role.toString()
)

