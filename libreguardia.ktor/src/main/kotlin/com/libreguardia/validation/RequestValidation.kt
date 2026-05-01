package com.libreguardia.validation

import com.libreguardia.validation.modules.academicYearValidation
import com.libreguardia.validation.modules.authValidation
import com.libreguardia.validation.modules.scheduleActivityValidation
import com.libreguardia.validation.modules.placeTypeValidation
import com.libreguardia.validation.modules.buildingValidation
import com.libreguardia.validation.modules.courseValidation
import com.libreguardia.validation.modules.groupValidation
import com.libreguardia.validation.modules.userValidation
import com.libreguardia.validation.modules.zoneValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        authValidation()
        userValidation()
        academicYearValidation()
        scheduleActivityValidation()
        placeTypeValidation()
        buildingValidation()
        groupValidation()
        zoneValidation()
        courseValidation()
    }
}