package com.libreguardia.validation.module

import com.libreguardia.dto.module.PlaceCreateDTO
import com.libreguardia.dto.module.PlaceEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.main.create.PlaceCreateField
import com.libreguardia.frontend.component.main.edit.PlaceEditField
import com.libreguardia.validation.validateRequired
import com.libreguardia.validation.validateUuid

fun PlaceEditDTO.validate(): MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[PlaceEditField.NAME] = validateRequired(this.name)
    //No vaalidation for floor, as it is not required and String type
    //errors[PlaceEditField.FLOOR] = validate()
    errors[PlaceEditField.BUILDING] = validateUuid(this.buildingId, false)
    errors[PlaceEditField.PLACE_TYPE] = validateUuid(this.placeTypeId, true)
    errors[PlaceEditField.ZONE] = validateUuid(this.zoneId, true)
    return errors
}

fun PlaceCreateDTO.validate():  MutableMap<FormField, String?> {
    val errors = mutableMapOf<FormField, String?>()
    errors[PlaceCreateField.NAME] = validateRequired(this.name)
    //No vaalidation for floor, as it is not required and String type
    //errors[PlaceCreateField.FLOOR] = validate()
    errors[PlaceCreateField.BUILDING] = validateUuid(this.buildingId, false)
    errors[PlaceCreateField.PLACE_TYPE] = validateUuid(this.placeTypeId, true)
    errors[PlaceCreateField.ZONE] = validateUuid(this.zoneId, true)
    return errors
}