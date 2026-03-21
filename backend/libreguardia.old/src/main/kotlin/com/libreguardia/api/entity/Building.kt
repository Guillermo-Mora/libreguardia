package com.libreguardia.api.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_building")
class Building : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    lateinit var name: String

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    var places: Set<Place> = mutableSetOf()
}