package com.libreguardia.routing


import com.libreguardia.routing.modules.academicYearRouting
import com.libreguardia.routing.modules.authRouting
import com.libreguardia.routing.modules.entryRouting
import com.libreguardia.routing.modules.groupRouting
import com.libreguardia.routing.modules.professionalFamilyRouting
import com.libreguardia.routing.modules.userRouting
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.AuthService
import com.libreguardia.service.GroupService
import com.libreguardia.service.ProfessionalFamilyService
import com.libreguardia.service.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.resources.*
import io.ktor.server.routing.*
fun Application.configureRouting(
    authService: AuthService,
    academicYearService: AcademicYearService,
    groupService: GroupService,
    professionalFamilyService: ProfessionalFamilyService,
    userService: UserService
) {
    //Unified routes for pages and for obtaining content (get, post, put, patch)
    install(Resources)
    routing {
        entryRouting()
        authRouting(
            authService = authService
        )
        userRouting(userService = userService)
        academicYearRouting(service = academicYearService)
        groupRouting(service = groupService)
        professionalFamilyRouting(service = professionalFamilyService)
        staticResources("/static", "static")
    }
}