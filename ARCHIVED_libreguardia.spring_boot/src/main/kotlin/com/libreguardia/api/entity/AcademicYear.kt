package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "tbl_academic_year")
class AcademicYear : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    lateinit var name: String

    @Column(name = "start_date", nullable = false)
    lateinit var startDate: LocalDate

    @Column(name = "end_date", nullable = false)
    lateinit var endDate: LocalDate

    @OneToMany(mappedBy = "academicYear", fetch = FetchType.LAZY)
    var courses: Set<Course> = mutableSetOf()

    @OneToMany(mappedBy = "academicYear", fetch = FetchType.LAZY)
    var schedules: MutableSet<Schedule> = mutableSetOf()
}