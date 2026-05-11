package com.libreguardia.frontend.component

import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.header
import kotlinx.html.nav
import kotlinx.html.span



@OptIn(ExperimentalKtorApi::class)
fun FlowContent.appHeader() {
    header("app-header") {
        div("app-header-left") {
            span("app-header-greeting") { text("Welcome back") }
            span("app-header-user") { text("User") }
        }
        div("app-header-right") {
            a(classes = "app-header-link") {
                attributes.hx {
                    get = "/user/profile"
                    replaceUrl = "true"
                    pushUrl = "true"
                    target = "#main-content"
                    swap = "innerHTML"
                }
                href = "/user/profile"
                text("My profile")
            }
            button {
                attributes["class"] = "btn btn-ghost btn-sm"
                attributes.hx {
                    trigger = "click"
                    post = "/auth/logout"
                }
                text("Logout")
            }
        }
    }
}
