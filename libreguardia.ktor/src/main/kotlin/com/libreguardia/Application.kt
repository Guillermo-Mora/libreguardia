package com.libreguardia

import at.favre.lib.crypto.bcrypt.BCrypt
import com.libreguardia.config.*
import com.libreguardia.config.database.configureDatabase
import com.libreguardia.config.database.configureFlyway
import com.libreguardia.config.exception.configureStatusPages
import com.libreguardia.module.absence.AbsenceRepository
import com.libreguardia.module.academic_year.AcademicYearRepository
import com.libreguardia.module.academic_year.AcademicYearService
import com.libreguardia.module.building.BuildingRepository
import com.libreguardia.module.building.BuildingService
import com.libreguardia.module.course.CourseRepository
import com.libreguardia.module.course.CourseService
import com.libreguardia.module.group.GroupRepository
import com.libreguardia.module.group.GroupService
import com.libreguardia.module.place.PlaceRepository
import com.libreguardia.module.place.PlaceService
import com.libreguardia.module.place_type.PlaceTypeRepository
import com.libreguardia.module.place_type.PlaceTypeService
import com.libreguardia.module.professional_family.ProfessionalFamilyRepository
import com.libreguardia.module.professional_family.ProfessionalFamilyService
import com.libreguardia.module.schedule.ScheduleRepository
import com.libreguardia.module.schedule_activity.ScheduleActivityRepository
import com.libreguardia.module.schedule_activity.ScheduleActivityService
import com.libreguardia.module.service.ServiceRepository
import com.libreguardia.module.auth.SessionRepository
import com.libreguardia.module.user.UserRepository
import com.libreguardia.module.user.UserService
import com.libreguardia.module.zone.ZoneRepository
import com.libreguardia.module.zone.ZoneService
import com.libreguardia.config.routing.configureRouting
import com.libreguardia.module.auth.AuthService
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
    val placeRepository = PlaceRepository()

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
        repository = placeTypeRepository
    )
    val zoneService = ZoneService(
        repository = zoneRepository
    )
    val groupService = GroupService(
        groupRepository = groupRepository,
        courseRepository = courseRepository
    )
    val courseService = CourseService(
        courseRepository = courseRepository,
        professionalFamilyRepository = professionalFamilyRepository
    )
    val professionalFamilyService = ProfessionalFamilyService(
        professionalFamilyRepository = professionalFamilyRepository
    )
    val placeService = PlaceService(
        placeRepository = placeRepository,
        buildingRepository = buildingRepository,
        zoneRepository = zoneRepository,
        placeTypeRepository = placeTypeRepository
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
        professionalFamilyService = professionalFamilyService,
        placeService = placeService
    )
}