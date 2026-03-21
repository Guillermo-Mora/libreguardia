package com.libreguardia.api.repository

import com.libreguardia.api.entity.TimeRange
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TimeRangeRepository : JpaRepository<TimeRange, UUID>