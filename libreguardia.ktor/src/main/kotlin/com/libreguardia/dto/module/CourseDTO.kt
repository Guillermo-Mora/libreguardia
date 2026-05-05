package com.libreguardia.dto.module

import com.libreguardia.db.model.CourseEntity
import com.libreguardia.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CourseResponseDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val professionalFamilyId: UUID,
)

@Serializable
data class CourseCreateDTO(
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val professionalFamilyId: UUID
)

@Serializable
data class CourseEditDTO(
    val name: String? = null
)

fun CourseEntity.toResponseDTO() = CourseResponseDTO(
    id = id.value,
    name = name,
    professionalFamilyId = professionalFamily.id.value,
)