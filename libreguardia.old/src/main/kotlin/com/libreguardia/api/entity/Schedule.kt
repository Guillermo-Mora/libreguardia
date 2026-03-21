package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_schedule")
class Schedule : BaseEntity() {
    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day", columnDefinition = "enum_week_day", nullable = false)
    lateinit var weekDay: WeekDay

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    var group: Group? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_activity_id", referencedColumnName = "id", nullable = false)
    lateinit var scheduleActivity: ScheduleActivity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", referencedColumnName = "id", nullable = false)
    lateinit var place: Place

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_range_id", referencedColumnName = "id", nullable = false)
    lateinit var timeRange: TimeRange

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    lateinit var user: User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", referencedColumnName = "id", nullable = false)
    lateinit var academicYear: AcademicYear

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    var absences: MutableSet<Absence> = mutableSetOf()
}