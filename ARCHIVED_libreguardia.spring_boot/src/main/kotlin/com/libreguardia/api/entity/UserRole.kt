package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_user_role")
class UserRole : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    lateinit var name: String

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    var users: Set<User> = mutableSetOf()
}