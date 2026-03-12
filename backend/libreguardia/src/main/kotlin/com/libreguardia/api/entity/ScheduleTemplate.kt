package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "tbl_schedule_template")
data class ScheduleTemplate (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schedule_template_id")
    val scheduleTemplateId: UUID? = null,

    @Column(name = "schedule_template_name", nullable = false, unique = true)
    val scheduleTemplaName: String = ""
)