package com.libreguardia.api.repository

import com.libreguardia.api.entity.Place
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PlaceRepository : JpaRepository<Place, UUID>