package com.libreguardia.api.repository

import com.libreguardia.api.entity.Building
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BuildingRepository : JpaRepository<Building, UUID>