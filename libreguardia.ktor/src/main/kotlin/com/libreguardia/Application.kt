package com.libreguardia

import com.libreguardia.config.*
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.repository.ProfessionalFamilyRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.repository.UserRoleRepository
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.ProfessionalFamilyService
import com.libreguardia.service.UserService
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = environment.config
    val dbUrl = config.property("storage.jdbcURL").getString()
    val dbUser = config.property("storage.user").getString()
    val dbPassword = config.property("storage.password").getString()

    val userRepository = UserRepository()
    val professionalFamilyRepository = ProfessionalFamilyRepository()
    val academicYearRepository = AcademicYearRepository()
    val absenceRepository = AbsenceRepository()
    val serviceRepository = ServiceRepository()
    val scheduleRepository = ScheduleRepository()
    val userRoleRepository = UserRoleRepository()

    val userService = UserService(
        userRepository = userRepository,
        absenceRepository = absenceRepository,
        serviceRepository = serviceRepository,
        scheduleRepository = scheduleRepository,
        userRoleRepository = userRoleRepository
    )
    val professionalFamilyService = ProfessionalFamilyService(
        repository = professionalFamilyRepository
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
    configureMonitoring()
    configureDefaultHeaders()
    configureCompression()
    configureStatusPages()
    configureSerialization()
    configureAuthentication()
    configureRouting(
        academicYearService = academicYearService,
        professionalFamilyService = professionalFamilyService,
        userService = userService
    )
}