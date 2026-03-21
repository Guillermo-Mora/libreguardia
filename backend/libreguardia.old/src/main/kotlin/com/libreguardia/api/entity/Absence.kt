package com.libreguardia.api.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "tbl_absence")
class Absence : BaseEntity() {
    @Column(name = "date", nullable = false)
    lateinit var date: LocalDate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    lateinit var schedule: Schedule

    @OneToOne(mappedBy = "absence", fetch = FetchType.LAZY)
    var service: Service? = null
}