package com.libreguardia.api.repository

import com.libreguardia.api.entity.ScheduleActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ScheduleActivityRepository : JpaRepository<ScheduleActivity, UUID>