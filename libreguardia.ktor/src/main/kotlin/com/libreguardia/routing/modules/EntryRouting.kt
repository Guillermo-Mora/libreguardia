package com.libreguardia.routing.modules

import com.libreguardia.frontend.page.dashBoardPage
import com.libreguardia.frontend.page.loginPage
import io.ktor.server.auth.authenticate
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.entryRouting() {
    get("/login") {
        call.respondHtml { loginPage() }
    }
    authenticate ("auth-session") {
        get("/") {
            call.respondHtml { dashBoardPage() }
        }
    }
}