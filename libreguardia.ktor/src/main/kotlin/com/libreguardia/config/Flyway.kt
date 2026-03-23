package com.libreguardia.config

import io.ktor.server.application.Application
import org.flywaydb.core.Flyway

fun Application.configureFlyway(
    url: String,
    user: String,
    password: String
) {
    Flyway.configure().dataSource(
        url,
        user,
        password
    ).load().migrate()
}