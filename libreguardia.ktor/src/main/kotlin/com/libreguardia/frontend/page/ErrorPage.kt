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
    body("error-body") {
        div("error-card") {
            div("error-code") { text("Error") }
            h1("error-title") { text("Something went wrong") }
            p("error-message") { text("$errorCode") }
            a(classes = "btn btn-primary") {
                href = "/"
                text("Go to dashboard")
            }
        }
        htmxScript()
    }
}
