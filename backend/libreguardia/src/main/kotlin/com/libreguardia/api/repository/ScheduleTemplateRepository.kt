package com.libreguardia.api.repository

import com.libreguardia.api.entity.ScheduleTemplate
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ScheduleTemplateRepository : JpaRepository<ScheduleTemplate, UUID>