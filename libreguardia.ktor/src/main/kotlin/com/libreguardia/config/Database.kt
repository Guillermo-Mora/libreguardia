package com.libreguardia.config

import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database

//Implementar connection pool
fun Application.configureDatabase(
    url: String,
    user: String,
    password: String
) {
    Database.connect(
        url = url,
        user = user,
        password = password
    )
}