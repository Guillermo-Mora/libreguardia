package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_schedule_activity")
class ScheduleActivity : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    lateinit var name: String

    @Column(name = "generates_service", nullable = false)
    var generatesService: Boolean = false

    @OneToMany(mappedBy = "scheduleActivity", fetch = FetchType.LAZY)
    var schedules: MutableSet<Schedule> = mutableSetOf()
}