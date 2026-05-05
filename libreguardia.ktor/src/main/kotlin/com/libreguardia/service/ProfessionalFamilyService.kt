package com.libreguardia.service

import com.libreguardia.model.ProfessionalFamilyModel
import com.libreguardia.repository.ProfessionalFamilyRepository
import com.libreguardia.util.withTransaction

class ProfessionalFamilyService(
    private val professionalFamilyRepository: ProfessionalFamilyRepository
) {
    suspend fun getAll(): List<ProfessionalFamilyModel> =
        withTransaction { professionalFamilyRepository.getAll() }
}