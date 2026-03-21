package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_course")
class Course : BaseEntity() {
    @Column(name = "name", nullable = false, length = 50)
    lateinit var name: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_family_id", referencedColumnName = "id", nullable = false)
    lateinit var professionalFamily: ProfessionalFamily

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", referencedColumnName = "id", nullable = false)
    lateinit var academicYear: AcademicYear

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    var groups: MutableSet<Group> = mutableSetOf()
}