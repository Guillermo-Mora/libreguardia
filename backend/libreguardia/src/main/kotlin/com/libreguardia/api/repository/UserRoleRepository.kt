package com.libreguardia.api.repository

import com.libreguardia.api.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRoleRepository : JpaRepository<UserRole,  UUID> {
    fun findByName(name: String): UserRole?
}