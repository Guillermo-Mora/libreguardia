package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_professional_family")
class ProfessionalFamily : BaseEntity() {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    lateinit var name: String

    @OneToMany(mappedBy = "professionalFamily", fetch = FetchType.LAZY)
    var courses: Set<Course> = mutableSetOf()
}