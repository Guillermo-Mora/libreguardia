package com.libreguardia.frontend.component.main

import com.libreguardia.model.UserModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.usersList(
    users: List<UserModel>
) {
    table("schedule-table") {
        tr {
            th {
                text("User")
            }
            th {
                text("Email")
            }
            th {
                text("Phone number")
            }
            th {
                text("Role")
            }
            th {
                text("Enabled")
            }
        }
        for (user in users)
            tr {
                td("td-filled") { text("${user.name} ${user.surname}") }
                td("td-filled") { text(user.email) }
                td("td-filled") { text(user.phoneNumber) }
                td("td-filled") { text(user.role) }
                td("td-filled") { text(if (user.isEnabled) "Yes" else "No") }
                td {
                    button {
                        attributes.hx {
                            get = "/user/${user.id}"
                            replaceUrl = "true"
                            pushUrl = "true"
                            target = "#main-content"
                            swap = "innerHTML"
                        }
                        text("Edit")
                    }
                }
            }
    }
}