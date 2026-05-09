package com.libreguardia.repository

import com.libreguardia.db.model.CourseEntity
import com.libreguardia.db.model.CourseTable
import com.libreguardia.db.model.ProfessionalFamilyTable
import com.libreguardia.dto.module.CourseCreateDTO
import com.libreguardia.dto.module.CourseEditDTO
import com.libreguardia.model.CourseModel
import com.libreguardia.model.toModel
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.dao.with
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import java.util.*

class CourseRepository : BaseRepository<CourseTable>(CourseTable) {
    //Specifying eager loading for the referenced professional families, to prevent N + 1 problem
    fun getAll(): List<CourseModel> =
        CourseEntity
            .all()
            .with(CourseEntity::professionalFamily)
            .map { it.toModel() }

    fun getThis(uuid: UUID): CourseModel? =
        CourseEntity
            .findById(uuid)
            ?.load(CourseEntity::professionalFamily)
            ?.toModel()

    fun save(
        courseCreateDTO: CourseCreateDTO
    ) {
        CourseTable.insert {
            it[name] = courseCreateDTO.name
            it[professionalFamily] = UUID.fromString(courseCreateDTO.professionalFamilyId)
        }
    }

    fun editThis(
        uuid: UUID,
        dto: CourseEditDTO
    ): Boolean {
        return CourseTable.update({ CourseTable.id eq uuid }) { updated ->
            updated[name] = dto.name
            updated[professionalFamily] = UUID.fromString(dto.professionalFamilyId)
        } == 1
    }

    fun isNameTaken(
        uuid: UUID,
        name: String
    ) =
        CourseTable
            .select(CourseTable.name)
            .where { CourseTable.id neq uuid and (CourseTable.name eq name) }
            .limit(1)
            .count().toInt() >= 1

    fun isNameTaken(
        name: String
    ) =
        CourseTable
            .select(CourseTable.name)
            .where { CourseTable.name eq name }
            .limit(1)
            .count().toInt() >= 1
}