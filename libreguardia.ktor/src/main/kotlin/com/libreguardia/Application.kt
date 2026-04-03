package com.libreguardia

import com.libreguardia.config.*
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.UserRepository
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
    val absenceRepository = AbsenceRepository()
    val serviceRepository = ServiceRepository()
    val scheduleRepository = ScheduleRepository()

    val userService = UserService(
        userRepository = userRepository,
        absenceRepository = absenceRepository,
        serviceRepository = serviceRepository,
        scheduleRepository = scheduleRepository
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
    configureMonitoring()
    configureDefaultHeaders()
    configureCompression()
    configureStatusPages()
    configureSerialization()
    configureAuthentication()
    configureRouting(
        userService = userService
    )

    //Test route
    testRoutes(PostgresTaskRepository())
}