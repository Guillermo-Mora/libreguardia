package com.libreguardia.frontend.component

import com.libreguardia.db.Role
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.nav
import kotlinx.html.span

data class MenuOption(
    val name: String,
    val path: String,
    val roles: Set<Role>
)

private val asideMenuOptions = listOf(
    MenuOption(
        name = "Dashboard",
        path = "/",
        roles = setOf(Role.VISUALIZER, Role.USER, Role.ADMIN)
    ),
    MenuOption(
        name = "My Absences",
        path = "/todo",
        roles = setOf(Role.USER, Role.ADMIN)
    ),
    MenuOption(
        name = "Live Services",
        path = "/todo",
        roles = setOf(Role.USER, Role.ADMIN)
    ),
    MenuOption(
        name = "Settings",
        path = "/settings",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Academic Years",
        path = "/academic-year",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Users",
        path = "/user",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Absences",
        path = "/absence",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Services",
        path = "/service",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Professional families",
        path = "/professional-family",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Courses",
        path = "/course",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Groups",
        path = "/group",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Buildings",
        path = "/building",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Zones",
        path = "/zone",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Place types",
        path = "/place-type",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Activities",
        path = "/schedule-activity",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Places",
        path = "/place",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Schedule Templates",
        path = "/schedule-template",
        roles = setOf(Role.ADMIN)
    ),
)

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.appAsideMenu(
    role: Role
) {
    aside("sidebar") {
        div("sidebar-header") {
            h1("sidebar-logo") { text("LibreGuardia") }
        }
        nav("sidebar-nav") {
            for (option in asideMenuOptions)
                if (role in option.roles) asideMenuOption(option)
        }
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.asideMenuOption(
    menuOption: MenuOption
) {
    div("sidebar-nav-item") {
        a(classes = "sidebar-link") {
            attributes.hx {
                get = menuOption.path
                replaceUrl = "true"
                pushUrl = "true"
                target = "#main-content"
                swap = "innerHTML"
            }
            href = menuOption.path
            span("sidebar-link-icon") { +" " }
            span("sidebar-link-text") { text(menuOption.name) }
        }
    }
}
