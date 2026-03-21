package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_schedule_template")
class ScheduleTemplate : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @OneToMany(mappedBy = "scheduleTemplate", fetch = FetchType.LAZY)
    var scheduleTemplateSlots: MutableSet<ScheduleTemplateSlot> = mutableSetOf()
}