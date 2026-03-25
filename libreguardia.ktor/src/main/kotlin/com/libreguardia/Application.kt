package com.libreguardia

import com.libreguardia.config.*
import com.libreguardia.user.PostgresTaskRepository
import com.libreguardia.user.testRoutes
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = environment.config
    val url = config.property("storage.jdbcURL").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    configureDatabase(
        url = url,
        user = user,
        password = password
    )
    configureFlyway(
        url = url,
        user = user,
        password = password
    )
    configureDefaultHeaders()
    configureCompression()
    configureStatusPage()
    configureSerialization()
    configureAuthentication()
    configureRouting()
    //Test route
    testRoutes(PostgresTaskRepository())
}