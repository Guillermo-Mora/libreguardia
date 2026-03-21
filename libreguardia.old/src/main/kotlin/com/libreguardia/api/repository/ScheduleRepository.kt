package com.libreguardia.api.repository

import com.libreguardia.api.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ScheduleRepository : JpaRepository<Schedule, UUID>