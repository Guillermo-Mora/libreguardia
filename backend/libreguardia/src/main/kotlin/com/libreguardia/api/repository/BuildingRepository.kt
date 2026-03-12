package com.libreguardia.api.repository

import com.libreguardia.api.entity.Building
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BuildingRepository : JpaRepository<Building, UUID>