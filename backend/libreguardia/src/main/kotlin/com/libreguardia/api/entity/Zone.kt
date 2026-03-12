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
@Table(name = "tbl_zone")
data class Zone (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "zone_id")
    val zoneId: UUID? = null,

    @Column(name = "zone_name", nullable = false, unique = true, length = 50)
    val zoneName: String = "",

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    val places: Set<Place>
)