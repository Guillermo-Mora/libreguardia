package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.exception.InsufficientPermissionsException
import com.libreguardia.frontend.component.main.dashboard
import com.libreguardia.frontend.page.loginPage
import com.libreguardia.routing.respondHtmlPage
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.entryRouting() {
    get("/login") {
        call.respondHtml { loginPage() }
    }
    authenticate(AUTH_SESSION) {
        authorized(Role.USER, Role.ADMIN, Role.VISUALIZER) {
            get("/") {
                //Temporary error thrown
                val role = call.principal<UserPrincipal>()?.userRole ?: throw InsufficientPermissionsException()
                respondHtmlPage(
                    role = role,
                    content = {
                        dashboard(
                            role = role
                        )
                    }
                )
            }
        }
    }
}