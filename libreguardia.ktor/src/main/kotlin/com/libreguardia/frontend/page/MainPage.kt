package com.libreguardia.frontend.page

import com.libreguardia.db.Role
import com.libreguardia.frontend.component.*
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun HTML.mainPage(
    role: Role,
    mainContent: FlowContent.() -> Unit
) {
    head {
        title { text("Libreguardia ~ dashboard") }
        meta { charset = "utf-8" }
        meta { name = "viewport"; content = "width=device-width, initial-scale=1.0" }
        link { rel = "stylesheet"; href = "/static/frontend/css/style.css" }
    }
    body {
        appHeader()
        appAsideMenu(role = role)
        main {
            id = "main-content"
            mainContent()
        }
        htmxScript()
    }
}