package com.libreguardia.api.repository

import com.libreguardia.api.entity.PlaceType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlaceTypeRepository : JpaRepository<PlaceType, UUID>