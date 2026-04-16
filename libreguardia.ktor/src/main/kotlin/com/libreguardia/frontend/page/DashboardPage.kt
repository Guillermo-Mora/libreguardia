package com.libreguardia.frontend.page

import com.libreguardia.frontend.component.*
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun HTML.dashBoardPage() {
    head {
        title { text("Libreguardia ~ dashboard") }
        meta {  charset="utf-8" }
        meta { name="viewport"; content="width=device-width, initial-scale=1.0" }
        link { rel= "stylesheet"; href = "/static/frontend/css/style.css" }
    }
    body {
        appHeader()
        main {
            p { text("Logeado correctamente") }
        }
        htmxScript()
    }
}

private val menuOptions = listOf(
    "Dashboard",
    "Absences",
    "Services",
    "Administration"
)

private fun FlowContent.appHeader() {
    header(classes = "app-header") {
        nav(classes = "app-nav") {
            menuOptions.forEach {
                div {
                    a {
                        href = ""
                        text(it)
                    }
                }
            }
        }
    }
}