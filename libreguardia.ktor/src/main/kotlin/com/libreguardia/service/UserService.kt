package com.libreguardia.service

import com.libreguardia.config.withTransaction
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.repository.UserRepository

class UserService (
    private val userRepository: UserRepository
) {
    suspend fun getAllUsers(): List<UserResponseDTO> = withTransaction {
        //Recordar que no debo incluir lógica de negocio dentro de las transacciones
        return@withTransaction userRepository.allUsers()
    }
}