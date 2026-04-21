package com.libreguardia.frontend.component

import com.libreguardia.db.Role
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.div



private val generalAsideMenuOptions = listOf(
    "Dashboard",
    "My Absences",
    "Live Services",
)

private val adminAsideMenuOptions = listOf(
    //It will be easier to start with these dedicated pages. In the future, I could mix some of them
    // to make it easier to understand and navigate through the admin pages and the system. But for now, this
    // will be much easier to implement.
    //
    //Here in settings, for example, if each user can alter his own schedule, or activate the selectable mode,
    // where you can select group of users that can do it, etc. To implement in the future.
    "Settings",
    //
    "Academic Years",
    //
    "Users",
    //
    "Absences",
    "Services",
    //
    "Professional families",
    "Courses",
    "Groups",
    //
    "Buildings",
    "Zones",
    "Place types",
    "Activities",
    "Places",
    //
    "Schedule Templates"
)

fun FlowContent.appAsideMenu(
    role: Role
) {
    aside {
        generalAsideMenuOptions.forEach {
            div {
                a {
                    href = ""
                    text(it)
                }
            }
        }
        if (role == Role.ADMIN) adminAsideMenuOptions.forEach {
            div {
                a {
                    href = ""
                    text(it)
                }
            }
        }
    }
}