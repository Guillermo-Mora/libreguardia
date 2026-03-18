package com.libreguardia.api.repository

import com.libreguardia.api.entity.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = ["userRole"])
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    @Query("SELECT u.userRole.name FROM User u WHERE u.email = :email")
    fun findRoleNameByEmail(email: String): String
}