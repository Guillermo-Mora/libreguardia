package com.libreguardia.dto

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