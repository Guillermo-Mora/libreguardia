package com.libreguardia

import com.libreguardia.config.configureAuthentication
import com.libreguardia.config.configureCompression
import com.libreguardia.config.configureDatabase
import com.libreguardia.config.configureDefaultHeaders
import com.libreguardia.config.configureFlyway
import com.libreguardia.config.configureRouting
import com.libreguardia.config.configureSerialization
import com.libreguardia.config.configureStatusPage
import com.libreguardia.user.PostgresTaskRepository
import com.libreguardia.user.testRoutes
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.routing.Routing

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
