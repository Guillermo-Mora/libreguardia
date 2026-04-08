package com.libreguardia

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.*
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.repository.UserRoleRepository
import com.libreguardia.service.AuthService
import com.libreguardia.service.JwtService
import com.libreguardia.service.UserService
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
    val userRoleRepository = UserRoleRepository()

    val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()

    val userService = UserService(
        bcryptVerifyer = bcryptVerifyer,
        userRepository = userRepository,
        absenceRepository = absenceRepository,
        serviceRepository = serviceRepository,
        scheduleRepository = scheduleRepository,
        userRoleRepository = userRoleRepository
    )
    val jwtService = JwtService(
        application = this,
        userRepository = userRepository
    )
    val authService = AuthService(
        bcryptVerifyer = bcryptVerifyer,
        userRepository = userRepository,
        jwtService = jwtService
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
    configureSecurity(
        jwtService = jwtService
    )
    configureMonitoring()
    configureDefaultHeaders()
    configureCompression()
    configureStatusPages()
    configureRequestValidation()
    configureSerialization()
    configureAuthentication()
    configureRouting(
        authService = authService,
        userService = userService
    )
}