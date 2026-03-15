package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_user")
class User : BaseEntity() {
    @Column(name = "name", nullable = false, length = 50)
    lateinit var name: String

    @Column(name = "surname", nullable = false, length = 50)
    lateinit var surname: String

    @Column(name = "email", nullable = false, unique = true, length = 50)
    lateinit var email: String

    @Column(name = "phone_number", nullable = false, length = 20)
    lateinit var phoneNumber: String

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true

    @Column(name = "password", nullable = false, length = 60)
    lateinit var password: String

    //Temporary changed to eager for login to work
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id", referencedColumnName = "id", nullable = false)
    lateinit var userRole: UserRole

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var schedules: MutableSet<Schedule> = mutableSetOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var services: MutableSet<Service> = mutableSetOf()
}