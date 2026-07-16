package com.libreguardia.config

/*
import com.libreguardia.module.academic_year.academicYearRouting
import com.libreguardia.module.auth.authRouting
import com.libreguardia.module.building.buildingRouting
import com.libreguardia.module.course.courseRouting
import com.libreguardia.module.entry.entryRouting
import com.libreguardia.module.schedule_activity.scheduleActivityRouting
import com.libreguardia.module.place_type.placeTypeRouting
import com.libreguardia.module.group.groupRouting
import com.libreguardia.module.place.placeRouting
import com.libreguardia.module.professional_family.professionalFamilyRouting
import com.libreguardia.module.user.userRouting
import com.libreguardia.module.zone.zoneRouting
import com.libreguardia.module.academic_year.AcademicYearService
import com.libreguardia.module.auth.AuthService
import com.libreguardia.module.schedule_activity.ScheduleActivityService
import com.libreguardia.module.place_type.PlaceTypeService
import com.libreguardia.module.building.BuildingService
import com.libreguardia.module.group.GroupService
import com.libreguardia.module.course.CourseService
import com.libreguardia.module.place.PlaceService
import com.libreguardia.module.professional_family.ProfessionalFamilyService
import com.libreguardia.module.user.UserService
import com.libreguardia.module.zone.ZoneService
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
    professionalFamilyService: ProfessionalFamilyService,
    placeService: PlaceService
) {
    //Unified routes for pages and for obtaining content (get, post, put, patch)
    install(Resources)
    routing {
        staticResources(
            remotePath = "/static",
            basePackage = "static"
        )
        entryRouting()
        authRouting(
            authService = authService
        )
        userRouting(
            userService = userService
        )
        academicYearRouting(
            academicYearService = academicYearService
        )
        professionalFamilyRouting(
            professionalFamilyService = professionalFamilyService
        )
        scheduleActivityRouting(
            service = scheduleActivityService
        )
        groupRouting(
            groupService = groupService,
            courseService = courseService,
            academicYearService = academicYearService
        )
        placeRouting(
            placeService = placeService,
            buildingService = buildingService,
            zoneService = zoneService,
            placeTypeService = placeTypeService
        )
        zoneRouting(
            service = zoneService
        )
        buildingRouting(
            service = buildingService
        )
        placeTypeRouting(
            service = placeTypeService
        )
        courseRouting(
            courseService = courseService,
            professionalFamilyService = professionalFamilyService
        )
        placeRouting(
            placeService = placeService,
            buildingService = buildingService,
            zoneService = zoneService,
            placeTypeService = placeTypeService
        )
    }
}

 */