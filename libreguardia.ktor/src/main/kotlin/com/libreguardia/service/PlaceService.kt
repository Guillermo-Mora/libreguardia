package com.libreguardia.service

import com.libreguardia.dto.module.PlaceCreateDTO
import com.libreguardia.dto.module.PlaceEditDTO
import com.libreguardia.dto.module.toModel
import com.libreguardia.frontend.component.main.create.PlaceCreateField
import com.libreguardia.frontend.component.main.edit.PlaceEditField
import com.libreguardia.model.PlaceModel
import com.libreguardia.repository.BuildingRepository
import com.libreguardia.repository.PlaceRepository
import com.libreguardia.repository.PlaceTypeRepository
import com.libreguardia.repository.ZoneRepository
import com.libreguardia.util.withTransaction
import com.libreguardia.validation.OperationResult
import com.libreguardia.validation.module.validate
import io.ktor.server.plugins.*
import java.util.*

class PlaceService(
    private val placeRepository: PlaceRepository,
    private val buildingRepository: BuildingRepository,
    private val zoneRepository: ZoneRepository,
    private val placeTypeRepository: PlaceTypeRepository
) {
    suspend fun getAll(): List<PlaceModel> = withTransaction { placeRepository.getAll() }

    suspend fun getThis(
        uuid: UUID
    ): PlaceModel =
        withTransaction { placeRepository.getThis(uuid) ?: throw NotFoundException() }


    //As this is the first table where I implemented the conversion to the data model after format validation,
    // I leave these comments in order to understand it in the future when I implement it to the rest of the tables.
    suspend fun create(
        placeCreateDTO: PlaceCreateDTO
    ): OperationResult {
        //The DTO consists of just Strings, as it receives the data from the HTML form,
        // so it has to be validated before converting to the desired data types.
        val errors = placeCreateDTO.validate()
        //If there are format errors, we return the errors.
        if (containsErrors(errors)) return OperationResult.Error(errors)
        //Now that the data (Strings) doesn't have format errors, we convert it to the model, with the
        // data types we want.
        val placeDML = placeCreateDTO.toModel()
        return withTransaction {
            //Now with the model, we do the business logic validations.
            if (placeRepository.isNameTaken(
                    name = placeDML.name
                )
            ) errors[PlaceCreateField.NAME] = "Name already taken"
            placeDML.buildingId?.let {
                if (!buildingRepository.exists(
                        uuid = it
                    )
                ) errors[PlaceCreateField.BUILDING] = "This building doesn't exists"
            }
            if (!zoneRepository.exists(
                    uuid = placeDML.zoneId
                )
            ) errors[PlaceCreateField.ZONE] = "This zone doesn't exists"
            if (!placeTypeRepository.exists(
                    uuid = placeDML.placeTypeId
                )
            ) errors[PlaceCreateField.PLACE_TYPE] = "This place type doesn't exists"
            //If errors occurred during the business logic validations, we return the errors.
            if (containsErrors(errors)) return@withTransaction OperationResult.Error(errors)
            //At this point, all the data has been validated and its correct, so we persist it.
            placeRepository.save(
                model = placeDML
            )
            //We return that the operation was performed successfully/
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun editThis(
        uuid: UUID,
        placeEditDTO: PlaceEditDTO
    ): OperationResult {
        val errors = placeEditDTO.validate()
        if (containsErrors(errors)) return OperationResult.Error(errors)
        val placeDML = placeEditDTO.toModel()
        return withTransaction {
            if (placeRepository.isNameTaken(
                    name = placeDML.name,
                    uuid = uuid
                )
            ) errors[PlaceEditField.NAME] = "Name already taken"
            placeDML.buildingId?.let {
                if (!buildingRepository.exists(
                        uuid = it
                    )
                ) errors[PlaceEditField.BUILDING] = "This building doesn't exists"
            }
            if (!zoneRepository.exists(
                    uuid = placeDML.zoneId
                )
            ) errors[PlaceEditField.ZONE] = "This zone doesn't exists"
            if (!placeTypeRepository.exists(
                    uuid = placeDML.placeTypeId
                )
            ) errors[PlaceEditField.PLACE_TYPE] = "This place type doesn't exists"
            if (containsErrors(errors)) return@withTransaction OperationResult.Error(errors)
            if (!placeRepository.updateThis(
                    uuid = uuid,
                    model = placeDML
                )
            ) throw NotFoundException()
            return@withTransaction OperationResult.Success()
        }
    }

    suspend fun deleteThis(
        uuid: UUID
    ) {
        withTransaction {
            if (!placeRepository.deleteThis(uuid)) throw NotFoundException()
        }
    }
}