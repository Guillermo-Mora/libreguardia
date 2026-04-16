package com.libreguardia.validation

import com.libreguardia.validation.modules.authValidation
import com.libreguardia.validation.modules.userValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        authValidation()
        userValidation()
    }
}