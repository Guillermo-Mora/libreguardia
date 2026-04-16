package com.libreguardia.frontend.page

import com.libreguardia.frontend.component.*
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)

fun HTML.loginPage() {
    head {
        title { text("Libreguardia ~ login") }
        meta {  charset="utf-8" }
        meta { name="viewport"; content="width=device-width, initial-scale=1.0" }
        link { rel= "stylesheet"; href = "/static/frontend/css/style.css" }
    }
    body {
        main {
            loginForm()
        }
        htmxScript()
    }
}