package com.libreguardia.routing


import com.libreguardia.routing.module.academicYearRouting
import com.libreguardia.routing.module.authRouting
import com.libreguardia.routing.module.buildingRouting
import com.libreguardia.routing.module.courseRouting
import com.libreguardia.routing.module.entryRouting
import com.libreguardia.routing.module.scheduleActivityRouting
import com.libreguardia.routing.module.placeTypeRouting
import com.libreguardia.routing.module.groupRouting
import com.libreguardia.routing.module.professionalFamilyRouting
import com.libreguardia.routing.module.userRouting
import com.libreguardia.routing.module.zoneRouting
import com.libreguardia.routing.module.validationRouting
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.AuthService
import com.libreguardia.service.ScheduleActivityService
import com.libreguardia.service.PlaceTypeService
import com.libreguardia.service.BuildingService
import com.libreguardia.service.GroupService
import com.libreguardia.service.CourseService
import com.libreguardia.service.ProfessionalFamilyService
import com.libreguardia.service.UserService
import com.libreguardia.service.ZoneService
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authService: AuthService,
    academicYearService: AcademicYearService,
    scheduleActivityService: ScheduleActivityService,
    placeTypeService: PlaceTypeService,
    buildingService: BuildingService,
    groupService: GroupService,
    zoneService: ZoneService,
    courseService: CourseService,
    userService: UserService,
    professionalFamilyService: ProfessionalFamilyService
) {
    //Unified routes for pages and for obtaining content (get, post, put, patch)
    install(Resources)
    routing {
        validationRouting()
        entryRouting()
        authRouting(authService = authService)
        userRouting(userService = userService)
        professionalFamilyRouting(professionalFamilyService = professionalFamilyService)
        academicYearRouting(service = academicYearService)
        scheduleActivityRouting(service = scheduleActivityService)
        groupRouting(service = groupService)
        zoneRouting(service = zoneService)
        buildingRouting(service = buildingService)
        placeTypeRouting(service = placeTypeService)
        courseRouting(
            courseService = courseService,
            professionalFamilyService = professionalFamilyService
        )
        staticResources("/static", "static")
    }
}