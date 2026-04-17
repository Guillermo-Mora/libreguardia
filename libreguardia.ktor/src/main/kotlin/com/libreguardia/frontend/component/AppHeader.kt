package com.libreguardia.frontend.component

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.header
import kotlinx.html.nav

private val headerMenuOptions = listOf(
    "My profile"
)

fun FlowContent.appHeader() {
    header {
        nav {
            headerMenuOptions.forEach {
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