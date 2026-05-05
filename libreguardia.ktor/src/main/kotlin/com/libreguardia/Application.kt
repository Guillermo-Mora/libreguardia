package com.libreguardia

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.*
import com.libreguardia.db.configureDatabase
import com.libreguardia.db.configureFlyway
import com.libreguardia.exception.configureStatusPages
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.repository.CourseRepository
import com.libreguardia.validation.configureRequestValidation
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.SessionRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.repository.*
import com.libreguardia.routing.configureRouting
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.AuthService
import com.libreguardia.service.CourseService
import com.libreguardia.service.UserService
import com.libreguardia.service.*
import com.libreguardia.validation.configureRequestValidation
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlin.time.Clock

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.main() {
    val config = environment.config
    val dbUrl = config.property("storage.jdbcURL").getString()
    val dbUser = config.property("storage.user").getString()
    val dbPassword = config.property("storage.password").getString()

    val userRepository = UserRepository()
    val absenceRepository = AbsenceRepository()
    val serviceRepository = ServiceRepository()
    val scheduleRepository = ScheduleRepository()
    val scheduleActivityRepository = ScheduleActivityRepository()
    val sessionRepository = SessionRepository()
    val academicYearRepository = AcademicYearRepository()
    val courseRepository = CourseRepository()
    val groupRepository = GroupRepository()
    val zoneRepository = ZoneRepository()
    val buildingRepository = BuildingRepository()
    val placeTypeRepository = PlaceTypeRepository()
    val professionalFamilyRepository = ProfessionalFamilyRepository()

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
        sessionRepository = sessionRepository
    )
    val authService = AuthService(
        bcryptVerifyer = bcryptVerifyer,
        bcryptHasher = bcryptHasher,
        clock = clock,
        userRepository = userRepository,
        sessionRepository = sessionRepository,
    )
    val academicYearService = AcademicYearService(
        repository = academicYearRepository
    )
    val scheduleActivityService = ScheduleActivityService(
        repository = scheduleActivityRepository
    )
    val buildingService = BuildingService(
        repository = buildingRepository
    )
    val placeTypeService = PlaceTypeService(
        placeTypeRepository = placeTypeRepository
    )
    val zoneService = ZoneService(
        repository = zoneRepository
    )
    val groupService = GroupService(
        repository = groupRepository
    )
    val courseService = CourseService(
        repository = courseRepository
    )
    val professionalFamilyService = ProfessionalFamilyService(
        professionalFamilyRepository = professionalFamilyRepository
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
        authService = authService
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
        buildingService = buildingService,
        groupService = groupService,
        scheduleActivityService = scheduleActivityService,
        courseService = courseService,
        userService = userService,
        zoneService = zoneService,
        placeTypeService = placeTypeService,
        professionalFamilyService = professionalFamilyService
    )
}