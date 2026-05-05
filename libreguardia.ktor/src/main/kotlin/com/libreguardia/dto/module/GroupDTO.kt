package com.libreguardia.dto.module

import com.libreguardia.db.model.GroupEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GroupResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val code: String,
    val pointsMultiplier: Double,
    @Serializable(with = UUIDSerializer::class)
    val courseId: UUID
)

@Serializable
data class GroupCreateDTO(
    val code: String,
    val pointsMultiplier: Double? = null,
    @Serializable(with = UUIDSerializer::class)
    val courseId: UUID
)

@Serializable
data class GroupEditDTO(
    val code: String? = null,
    val pointsMultiplier: Double? = null,
    @Serializable(with = UUIDSerializer::class)
    val courseId: UUID? = null
)

fun GroupEntity.toResponseDTO() = GroupResponseDTO(
    id = id.value,
    code = code,
    pointsMultiplier = pointsMultiplier.toDouble(),
    courseId = course.id.value
)
