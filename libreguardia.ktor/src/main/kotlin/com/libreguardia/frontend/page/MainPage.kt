package com.libreguardia.frontend.page

import com.libreguardia.db.Role
import com.libreguardia.frontend.component.*
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun HTML.mainPage(role: Role) {
    head {
        title { text("Libreguardia ~ dashboard") }
        meta { charset = "utf-8" }
        meta { name = "viewport"; content = "width=device-width, initial-scale=1.0" }
        link { rel = "stylesheet"; href = "/static/frontend/css/style.css" }
    }
    body {
        //TEMPORARY FOR TESTING
        if (role == Role.VISUALIZER) {
            appHeader()
            main {
                id = "main-content"
                p { text("LIVE SERVICES VISUALIZATION PAGE HERE") }
            }
        } else {
            appHeader()
            main {
                id = "main-content"
                appAsideMenu(role = role)
            }
        }
        htmxScript()
    }
}