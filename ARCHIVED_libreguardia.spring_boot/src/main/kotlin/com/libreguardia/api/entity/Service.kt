package com.libreguardia.api.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_service")
class Service : BaseEntity() {
    @Column(name = "is_signed", nullable = false)
    var isSigned: Boolean = false

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "absence_id", referencedColumnName = "id", nullable = false)
    lateinit var absence: Absence

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    lateinit var user: User
}