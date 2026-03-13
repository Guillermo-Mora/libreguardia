package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.UUID

@Entity
@Table(name = "tbl_zone")
class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "zone_id")
    var zoneId: UUID? = null

    @Column(name = "zone_name", nullable = false, unique = true, length = 50)
    var zoneName: String = ""

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    var places: MutableSet<Place> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Zone
        return zoneId != null && zoneId == other.zoneId
    }

    override fun hashCode() = this::class.hashCode()
}