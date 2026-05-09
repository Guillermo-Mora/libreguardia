package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_schedule_template_slot")
class ScheduleTemplateSlot : BaseEntity() {
    @Enumerated(EnumType.STRING)
    @Column(name = "week_day", columnDefinition = "enum_week_day", nullable = false)
    lateinit var weekDay: WeekDay

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_template_id", referencedColumnName = "id", nullable = false)
    lateinit var scheduleTemplate: ScheduleTemplate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    var group: Group? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_activity_id", referencedColumnName = "id")
    var scheduleActivity: ScheduleActivity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    var place: Place? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_range_id", referencedColumnName = "id", nullable = false)
    lateinit var timeRange: TimeRange
}