package com.libreguardia.api.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_place")
class Place : BaseEntity() {
    @Column(name = "name", nullable = false, length = 50)
    lateinit var name: String

    @Column(name = "floor", length = 50)
    var floor: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    var building: Building? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", referencedColumnName = "id", nullable = false)
    lateinit var zone: Zone

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_type_id", referencedColumnName = "id", nullable = false)
    lateinit var placeType: PlaceType

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    var schedules: MutableSet<Schedule> = mutableSetOf()
}