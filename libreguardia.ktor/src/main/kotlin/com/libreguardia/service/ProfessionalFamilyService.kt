package com.libreguardia.service

import com.libreguardia.dto.ProfessionalFamilyRequestDTO
import com.libreguardia.dto.ProfessionalFamilyResponseDTO
import com.libreguardia.repository.ProfessionalFamilyRepository
import java.util.UUID

class ProfessionalFamilyService(
    private val repository: ProfessionalFamilyRepository
) {
    fun getAll(): List<ProfessionalFamilyResponseDTO> = repository.all()

    fun getById(id: UUID): ProfessionalFamilyResponseDTO? = repository.findById(id)

    fun create(request: ProfessionalFamilyRequestDTO): ProfessionalFamilyResponseDTO = repository.save(request)

    fun update(id: UUID, request: ProfessionalFamilyRequestDTO): ProfessionalFamilyResponseDTO? = repository.update(id, request)

    fun delete(id: UUID): Boolean = repository.delete(id)
}
