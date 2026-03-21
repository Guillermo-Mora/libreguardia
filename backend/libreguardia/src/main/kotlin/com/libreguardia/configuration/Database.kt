package com.libreguardia.configuration

import io.ktor.server.application.*
import org.jetbrains.exposed.v1.jdbc.Database

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