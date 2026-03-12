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
data class PlaceType (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "place_type_id")
    val placeTypeId: UUID? = null,

    @Column(name = "place_type_name", nullable = false, unique = true, length = 50)
    val placeTypeName: String = "",

    @OneToMany(mappedBy = "placeType", fetch = FetchType.LAZY)
    val places: Set<Place>
)