package com.libreguardia.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.UserNotFoundException
import com.libreguardia.config.withTransaction
import com.libreguardia.db.UserRoleEntity
import com.libreguardia.db.UserRoleTable
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.config.UserRoleNotFoundException
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.repository.UserRepository
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID

class UserService (
    private val userRepository: UserRepository
) {
    suspend fun getAllUsers(): List<UserResponseDTO> = withTransaction {
        //Recordar que no debo incluir lógica de negocio dentro de las transacciones,
        // es decir. Operaciones que no afecten directamente a la transacción y unicidad de los datos.
        // Hashear una contraseña por ejemplo, debe ir fuera de la transacción.
        userRepository.all()
    }

    suspend fun getUser(
        userUUID: UUID
    ): UserResponseDTO {
        return withTransaction {
            val user = userRepository.getByUUID(userUUID) ?: throw UserNotFoundException(userUUID.toString())
            user
        }
    }

    suspend fun createUser(
        userCreateDTO: UserCreateDTO
    ) {
        val bcryptHasher = BCrypt.withDefaults()
        val hashedPassword = bcryptHasher.hashToString(
            10,
            userCreateDTO.password.toCharArray()
        )
        withTransaction {
            val userRoleEntity = UserRoleEntity.find { UserRoleTable.name eq userCreateDTO.userRole }.firstOrNull()
            if (userRoleEntity == null) throw UserRoleNotFoundException(userCreateDTO.userRole)
            userRepository.save(
                userCreateDTO = userCreateDTO,
                userRoleEntity = userRoleEntity,
                hashedPassword = hashedPassword
            )
        }
    }

    suspend fun editUser(
        userUUID: UUID,
        userEditDTO: UserEditDTO
    ) {
        withTransaction {

        }
    }
}