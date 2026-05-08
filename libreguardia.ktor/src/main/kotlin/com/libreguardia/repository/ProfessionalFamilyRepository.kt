package com.libreguardia.repository

import com.libreguardia.db.Role
import com.libreguardia.db.model.ProfessionalFamilyEntity
import com.libreguardia.db.model.ProfessionalFamilyTable
import com.libreguardia.db.model.UserTable
import com.libreguardia.db.model.UserTable.email
import com.libreguardia.db.model.UserTable.isEnabled
import com.libreguardia.db.model.UserTable.name
import com.libreguardia.db.model.UserTable.password
import com.libreguardia.db.model.UserTable.phoneNumber
import com.libreguardia.db.model.UserTable.role
import com.libreguardia.db.model.UserTable.surname
import com.libreguardia.dto.module.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.module.ProfessionalFamilyEditDTO
import com.libreguardia.model.ProfessionalFamilyModel
import com.libreguardia.model.toModel
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class ProfessionalFamilyRepository {
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

    fun deleteThis(
        uuid: UUID
    ) =
        ProfessionalFamilyTable
            .deleteWhere { ProfessionalFamilyTable.id eq uuid } == 1

    fun save(
        professionalFamilyCreateDTO: ProfessionalFamilyCreateDTO
    ) {
        ProfessionalFamilyTable.insert {
            it[ProfessionalFamilyTable.name] = professionalFamilyCreateDTO.name
        }
    }

    fun exists(
        uuid: UUID
    ): Boolean =
        ProfessionalFamilyTable
            .select(ProfessionalFamilyTable.id)
            .where { ProfessionalFamilyTable.id eq uuid }
            .limit(1)
            .count().toInt() >= 1
}