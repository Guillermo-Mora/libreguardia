package com.libreguardia.api.repository

import com.libreguardia.api.entity.Zone
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ZoneRepository : JpaRepository<Zone, UUID>