package com.libreguardia.api.dto

class LogintRequestDto (
    val email: String,
    val password: String
)

class LoginResponseDto (
    val message: String,
    val token: String?,
)