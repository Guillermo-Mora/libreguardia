package com.libreguardia.frontend.component.main

import com.libreguardia.db.Role
import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.dashboard(role: Role) {
    //These texts just for testing
    p { text("Dashboard for '${role.name}' user type") }
    when(role) {
        Role.VISUALIZER -> {
            p { text("Visualizer dashboard")}
        }
        Role.USER -> {
            p { text("User dashboard")}
        }
        Role.ADMIN -> {
            p { text("Admin dashboard")}
        }
    }
}