package com.libreguardia.validation

import com.libreguardia.validation.module.academicYearValidation
import com.libreguardia.validation.module.authValidation
import com.libreguardia.validation.module.scheduleActivityValidation
import com.libreguardia.validation.module.placeTypeValidation
import com.libreguardia.validation.module.buildingValidation
import com.libreguardia.validation.module.courseValidation
import com.libreguardia.validation.module.groupValidation
import com.libreguardia.validation.module.zoneValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        authValidation()
        academicYearValidation()
        scheduleActivityValidation()
        placeTypeValidation()
        buildingValidation()
        groupValidation()
        zoneValidation()
        courseValidation()
    }
}