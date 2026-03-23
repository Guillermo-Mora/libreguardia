package com.libreguardia.api.entity

import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "tbl_time_range")
class TimeRange : BaseEntity() {
    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true

    @Column(name = "start_time", nullable = false)
    lateinit var startTime: LocalTime

    @Column(name = "end_time", nullable = false)
    lateinit var endTime: LocalTime

    @OneToMany(mappedBy = "timeRange", fetch = FetchType.LAZY)
    var schedules: MutableSet<Schedule> = mutableSetOf()
}