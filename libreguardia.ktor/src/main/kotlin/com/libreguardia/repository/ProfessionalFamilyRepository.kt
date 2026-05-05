package com.libreguardia.repository

import com.libreguardia.db.model.ProfessionalFamilyEntity
import com.libreguardia.model.ProfessionalFamilyModel
import com.libreguardia.model.toModel

class ProfessionalFamilyRepository {
    fun getAll(): List<ProfessionalFamilyModel> =
        ProfessionalFamilyEntity
            .all()
            .map(ProfessionalFamilyEntity::toModel)

}