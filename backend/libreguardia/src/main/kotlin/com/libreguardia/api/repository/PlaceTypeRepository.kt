package com.libreguardia.api.repository

import com.libreguardia.api.entity.PlaceType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceTypeRepository : JpaRepository<PlaceType, UUID>