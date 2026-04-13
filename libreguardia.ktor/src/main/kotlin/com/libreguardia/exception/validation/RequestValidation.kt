package com.libreguardia.exception.validation

import com.libreguardia.exception.validation.modules.authValidation
import com.libreguardia.exception.validation.modules.userValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        authValidation()
        userValidation()
    }
}