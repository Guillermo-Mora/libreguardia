package com.libreguardia.api.repository

import com.libreguardia.api.entity.ProfessionalFamily
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfessionalFamilyRepository : JpaRepository<ProfessionalFamily, UUID>