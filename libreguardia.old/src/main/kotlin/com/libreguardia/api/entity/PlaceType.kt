package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "tbl_place_type")
class PlaceType : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    lateinit var name: String

    @OneToMany(mappedBy = "placeType", fetch = FetchType.LAZY)
    var places: Set<Place> = mutableSetOf()
}