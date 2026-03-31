package com.libreguardia

import com.libreguardia.config.*
import com.libreguardia.repository.ProfessionalFamilyRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.service.ProfessionalFamilyService
import com.libreguardia.service.UserService
import com.libreguardia.user.PostgresTaskRepository
import com.libreguardia.user.testRoutes
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

//It would be better to separate the app in different modules in the future. For better responsability
// separations.
fun Application.module() {
    val config = environment.config
    val dbUrl = config.property("storage.jdbcURL").getString()
    val dbUser = config.property("storage.user").getString()
    val dbPassword = config.property("storage.password").getString()

    val userRepository = UserRepository()
    val professionalFamilyRepository = ProfessionalFamilyRepository()

    val userService = UserService(
        userRepository = userRepository
    )
    val professionalFamilyService = ProfessionalFamilyService(
        repository = professionalFamilyRepository
    )

    configureDatabase(
        url = dbUrl,
        user = dbUser,
        password = dbPassword
    )
    configureFlyway(
        url = dbUrl,
        user = dbUser,
        password = dbPassword
    )
    configureDefaultHeaders()
    configureCompression()
    configureStatusPage()
    configureSerialization()
    configureAuthentication()
    configureRouting(
        professionalFamilyService = professionalFamilyService,
        userService = userService
    )

    //Test route
    testRoutes(PostgresTaskRepository())
}