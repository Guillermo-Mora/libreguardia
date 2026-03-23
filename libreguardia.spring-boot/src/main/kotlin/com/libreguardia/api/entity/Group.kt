package com.libreguardia.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tbl_group")
class Group : BaseEntity() {
    @Column(name = "name", nullable = false, length = 50)
    lateinit var name: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    lateinit var course: Course

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    var schedules: MutableSet<Schedule> = mutableSetOf()
}