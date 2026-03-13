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
class Zone : BaseEntity() {
    @Column(name = "zone_name", nullable = false, unique = true, length = 50)
    var zoneName: String = ""

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    var places: MutableSet<Place> = mutableSetOf()
}