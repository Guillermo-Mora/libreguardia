package com.libreguardia.frontend.component

import com.libreguardia.db.Role
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.div

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
    //It will be easier to start with these dedicated pages. In the future, I could mix some of them
    // to make it easier to understand and navigate through the admin pages and the system. But for now, this
    // will be much easier to implement.
    //
    //Here in settings, for example, if each user can alter his own schedule, or activate the selectable mode,
    // where you can select group of users that can do it, etc. To implement in the future.
    MenuOption(
        name = "Settings",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    //
    MenuOption(
        name = "Academic Years",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    //
    MenuOption(
        name = "Users",
        path = "/user",
        roles = setOf(Role.ADMIN)
    ),
    //
    MenuOption(
        name = "Absences",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Services",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    //
    MenuOption(
        name = "Professional families",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Courses",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Groups",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    //
    MenuOption(
        name = "Buildings",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Zones",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Place types",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Activities",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    MenuOption(
        name = "Places",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
    //
    MenuOption(
        name = "Schedule Templates",
        path = "/todo",
        roles = setOf(Role.ADMIN)
    ),
)

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.appAsideMenu(
    role: Role
) {
    aside {
        for (option in asideMenuOptions)
            if (role in option.roles) asideMenuOption(option)
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.asideMenuOption(
    menuOption: MenuOption
) {
    div {
        a {
            attributes.hx {
                get = menuOption.path
                replaceUrl = "true"
                pushUrl = "true"
                target = "#main-content"
                swap = "innerHTML"
            }
            href = menuOption.path
            text(menuOption.name)
        }
    }
}