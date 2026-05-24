package com.libreguardia.frontend.component.main

import com.libreguardia.db.Role
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.p

fun FlowContent.dashboard(role: Role) {
    div("dashboard-grid") {
        div("dashboard-card") {
            div("dashboard-card-content") {
                h2("dashboard-card-title") { text("Welcome to LibreGuardia") }
                p("dashboard-card-subtitle") { text("Dashboard for '${role.name}' user type") }
            }
        }
        when(role) {
            Role.VISUALIZER -> {
                div("dashboard-card dashboard-card-accent") {
                    div("dashboard-card-content") {
                        p("dashboard-card-text") { text("You have view-only access. Browse schedules and services.") }
                    }
                }
            }
            Role.USER -> {
                div("dashboard-card dashboard-card-accent") {
                    div("dashboard-card-content") {
                        p("dashboard-card-text") { text("Manage your absences and view your live services.") }
                    }
                }
            }
            Role.ADMIN -> {
                div("dashboard-card dashboard-card-accent") {
                    div("dashboard-card-content") {
                        p("dashboard-card-text") { text("Dashboard to implement.") }
                    }
                }
            }
        }
    }
}
