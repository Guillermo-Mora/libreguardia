package com.libreguardia.api.repository

import com.libreguardia.api.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRoleRepository : JpaRepository<UserRole,  UUID> {
    fun findByName(name: String): UserRole?
}