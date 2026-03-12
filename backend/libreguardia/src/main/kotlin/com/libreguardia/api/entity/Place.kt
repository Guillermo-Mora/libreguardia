package com.libreguardia.api.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tbl_place")
data class Place (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "place_id")
    val placeId: UUID? = null,

    @Column(name = "place_name", nullable = false, length = 50)
    val placeName: String = "",

    @Column(name = "place_floor", length = 50)
    val placeFloor: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_fk", referencedColumnName = "building_id")
    val building: Building? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_fk", referencedColumnName = "zone_id", nullable = false)
    val zone: Zone,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_type_fk", referencedColumnName = "place_type_id", nullable = false)
    val placeType: PlaceType,
)