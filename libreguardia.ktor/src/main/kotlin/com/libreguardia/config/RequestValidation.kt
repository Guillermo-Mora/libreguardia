package com.libreguardia.config

import com.libreguardia.validation.authValidation
import com.libreguardia.validation.userValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        authValidation()
        userValidation()
    }
}