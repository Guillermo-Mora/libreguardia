package com.libreguardia.validation

import com.libreguardia.validation.module.placeTypeValidation
import com.libreguardia.validation.module.buildingValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        placeTypeValidation()
        buildingValidation()
    }
}