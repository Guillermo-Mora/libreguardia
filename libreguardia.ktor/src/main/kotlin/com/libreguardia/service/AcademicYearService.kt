package com.libreguardia.service

import com.libreguardia.dto.AcademicYearRequestDTO
import com.libreguardia.dto.AcademicYearResponseDTO
import com.libreguardia.repository.AcademicYearRepository
import java.util.UUID

class AcademicYearService(
    private val repository: AcademicYearRepository
) {
    fun getAll(): List<AcademicYearResponseDTO> = repository.all()

    fun getById(id: UUID): AcademicYearResponseDTO? = repository.findById(id)

    fun create(request: AcademicYearRequestDTO): AcademicYearResponseDTO = repository.save(request)

    fun update(id: UUID, request: AcademicYearRequestDTO): AcademicYearResponseDTO? = repository.update(id, request)

    fun delete(id: UUID): Boolean = repository.delete(id)
}
