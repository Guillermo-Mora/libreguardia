package com.libreguardia.dto.module

import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class RefreshTokenDTO(
    val refreshToken: String
)