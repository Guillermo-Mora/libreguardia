package com.libreguardia.frontend.component

import com.libreguardia.db.Role
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.div



private val generalAsideMenuOptions = listOf(
    "Dashboard",
    "My Absences",
    "Services",
)

private val adminAsideMenuOptions = listOf(
    "Users",
    "Absences",
    "Services",
    "Courses and Groups",
    "Academic Years",
    "Buildings",
    "Places",
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