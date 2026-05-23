package com.libreguardia.module.professional_family

import com.libreguardia.module.shared.repository.BaseRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ProfessionalFamilyRepository : BaseRepository<ProfessionalFamilyTable>(ProfessionalFamilyTable) {
    fun getAll(): List<ProfessionalFamilyModel> =
        ProfessionalFamilyEntity
            .all()
            .map { it.toModel() }

    fun getThis(
        uuid: UUID
    ) =
        ProfessionalFamilyEntity
            .findById(uuid)
            ?.toModel()

    fun isNameTaken(
        uuid: UUID,
        name: String
    ) =
        ProfessionalFamilyTable
            .select(ProfessionalFamilyTable.name)
            .where { ProfessionalFamilyTable.id neq uuid and (ProfessionalFamilyTable.name eq name) }
            .limit(1)
            .count().toInt() >= 1

    fun isNameTaken(
        name: String
    ) =
        ProfessionalFamilyTable
            .select(ProfessionalFamilyTable.name)
            .where { ProfessionalFamilyTable.name eq name }
            .limit(1)
            .count().toInt() >= 1

    fun editThis(
        uuid: UUID,
        professionalFamilyEditDTO: ProfessionalFamilyEditDTO
    ) =
        ProfessionalFamilyTable
            .update({ ProfessionalFamilyTable.id eq uuid }) {
                it[name] = professionalFamilyEditDTO.name
            } == 1

    fun save(
        professionalFamilyCreateDTO: ProfessionalFamilyCreateDTO
    ) {
        ProfessionalFamilyTable.insert {
            it[ProfessionalFamilyTable.name] = professionalFamilyCreateDTO.name
        }
    }
}