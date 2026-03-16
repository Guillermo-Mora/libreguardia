package com.libreguardia.api.dto

class LogintRequestDto (
    val email: String,
    val password: String
)

class LoginResponseDto (
    val success: Boolean,
    val message: String,
    val token: String?,
)