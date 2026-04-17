package com.libreguardia

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.*
import com.libreguardia.db.configureDatabase
import com.libreguardia.db.configureFlyway
import com.libreguardia.exception.configureStatusPages
import com.libreguardia.exception.validation.configureRequestValidation
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.repository.RefreshTokenRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.routing.configureRouting
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.AuthService
import com.libreguardia.service.JwtService
import com.libreguardia.service.UserService
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlin.time.Clock

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = environment.config
    val dbUrl = config.property("storage.jdbcURL").getString()
    val dbUser = config.property("storage.user").getString()
    val dbPassword = config.property("storage.password").getString()

    val userRepository = UserRepository()
    val absenceRepository = AbsenceRepository()
    val serviceRepository = ServiceRepository()
    val scheduleRepository = ScheduleRepository()
    val refreshTokenRepository = RefreshTokenRepository()
    val academicYearRepository = AcademicYearRepository()

    val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()
    val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
    val clock = Clock.System

    val userService = UserService(
        bcryptVerifyer = bcryptVerifyer,
        bcryptHasher = bcryptHasher,
        clock = clock,
        userRepository = userRepository,
        absenceRepository = absenceRepository,
        serviceRepository = serviceRepository,
        scheduleRepository = scheduleRepository,
        refreshTokenRepository = refreshTokenRepository
    )
    val jwtService = JwtService(
        //application = this,
        userRepository = userRepository
    )
    val authService = AuthService(
        bcryptVerifyer = bcryptVerifyer,
        bcryptHasher = bcryptHasher,
        clock = clock,
        userRepository = userRepository,
        jwtService = jwtService,
        refreshTokenRepository = refreshTokenRepository
    )
    val academicYearService = AcademicYearService(
        repository = academicYearRepository
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
    configureRouting(
        authService = authService,
        academicYearService = academicYearService,
        userService = userService
    )
}