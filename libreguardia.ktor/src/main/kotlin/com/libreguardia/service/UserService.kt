package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.withTransaction
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.db.UserRoleTable
import com.libreguardia.dto.UserRequestDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.exception.UserRoleNotFoundException
import com.libreguardia.repository.UserRepository
import org.jetbrains.exposed.v1.core.eq

class UserService (
    private val userRepository: UserRepository
) {
    suspend fun getAllUsers(): List<UserResponseDTO> = withTransaction {
        //Recordar que no debo incluir lógica de negocio dentro de las transacciones,
        // es decir. Operaciones que no afecten directamente a la transacción y unicidad de los datos.
        // Hashear una contraseña por ejemplo, debe ir fuera de la transacción.
        userRepository.all()
    }

    suspend fun createUser(
        userRequestDTO: UserRequestDTO
    ) {
        val bcryptHasher = BCrypt.withDefaults()
        val hashedPassword = bcryptHasher.hashToString(
            10,
            userRequestDTO.password.toCharArray()
        )
        withTransaction {
            val userRoleEntity = UserRoleEntity.find { UserRoleTable.name eq userRequestDTO.userRole }.firstOrNull()
            if (userRoleEntity == null) throw UserRoleNotFoundException()
            userRepository.save(
                userRequestDTO = userRequestDTO,
                userRoleEntity = userRoleEntity,
                hashedPassword = hashedPassword
            )
        }
    }
}