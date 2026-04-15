package com.libreguardia.service

import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.dto.AcademicYearResponseDTO
import com.libreguardia.exception.AcademicYearNotFoundException
import com.libreguardia.repository.AcademicYearRepository

class AcademicYearService(
    private val repository: AcademicYearRepository
) {
    fun getAll(): List<AcademicYearResponseDTO> = repository.getAll()

    fun getByUUID(uuid: java.util.UUID): AcademicYearResponseDTO = 
        repository.getByUUID(uuid) ?: throw AcademicYearNotFoundException()

    fun create(dto: AcademicYearCreateDTO): AcademicYearResponseDTO = repository.save(dto)

    fun update(uuid: java.util.UUID, dto: AcademicYearEditDTO) {
        if (!repository.update(uuid, dto)) throw AcademicYearNotFoundException()
    }

    fun delete(uuid: java.util.UUID) {
        if (!repository.delete(uuid)) throw AcademicYearNotFoundException()
    }

    fun toggleEnabled(uuid: java.util.UUID, enabled: Boolean) {
        if (!repository.toggleEnabled(uuid, enabled)) throw AcademicYearNotFoundException()
    }
}
