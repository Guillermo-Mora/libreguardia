package com.libreguardia.api.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_zone")
class Zone : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    lateinit var name: String

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    var places: MutableSet<Place> = mutableSetOf()
}