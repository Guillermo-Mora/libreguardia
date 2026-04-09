package com.libreguardia.service

import com.libreguardia.dto.CourseRequestDTO
import com.libreguardia.dto.CourseResponseDTO
import com.libreguardia.repository.CourseRepository
import java.util.UUID

class CourseService(
    private val repository: CourseRepository
) {
    fun getAll(): List<CourseResponseDTO> = repository.all()

    fun getById(id: UUID): CourseResponseDTO? = repository.findById(id)

    fun create(request: CourseRequestDTO): CourseResponseDTO = repository.save(request)

    fun update(id: UUID, request: CourseRequestDTO): CourseResponseDTO? = repository.update(id, request)

    fun delete(id: UUID): Boolean = repository.delete(id)
}