package com.libreguardia.api.dto

class UserCreateRequestDto(
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val active: Boolean,
    val password: String,
    val userRoleName: String
)

class UserToggleEnabledRequestDto(
    val email: String,
    val isActive: Boolean
)

class UserDeleteRequestDto(
    val email: String
)