package com.libreguardia.frontend.component

import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.header
import kotlinx.html.nav



@OptIn(ExperimentalKtorApi::class)
fun FlowContent.appHeader() {
    header {
        nav {
            div {
                a {
                    attributes.hx {
                        get = "/user/profile"
                        replaceUrl = "true"
                        pushUrl = "true"
                        target = "#main-content"
                        swap = "innerHTML"
                    }
                    text("My profile")
                }
            }
        }
    }
}