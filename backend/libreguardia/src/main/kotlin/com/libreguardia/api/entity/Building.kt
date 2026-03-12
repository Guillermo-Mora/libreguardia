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
@Table(name = "tbl_building")
data class Building (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "building_id")
    val buildingId: UUID? = null,

    @Column(name = "building_name", nullable = false, unique = true, length = 50)
    val buildingName: String = "",

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    val places: Set<Place>
)