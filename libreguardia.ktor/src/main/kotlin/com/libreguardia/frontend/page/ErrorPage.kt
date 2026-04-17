package com.libreguardia.frontend.page

import com.libreguardia.exception.ErrorCode
import com.libreguardia.frontend.component.htmxScript
import io.ktor.utils.io.*
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun HTML.errorPage(errorCode: ErrorCode) {
    head {
        title { text("Libreguardia ~ error") }
        meta { charset = "utf-8" }
        meta { name = "viewport"; content = "width=device-width, initial-scale=1.0" }
        link { rel = "stylesheet"; href = "/static/frontend/css/style.css" }
    }
    body {
        main {
            p { text("Error: $errorCode") }
        }
        htmxScript()
    }
}