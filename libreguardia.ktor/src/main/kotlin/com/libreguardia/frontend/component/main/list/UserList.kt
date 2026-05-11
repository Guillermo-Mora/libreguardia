package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.UserModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.userList(
    users: List<UserModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Users") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/user/new"
                    replaceUrl = "true"
                    pushUrl = "true"
                    target = "#main-content"
                    swap = "innerHTML"
                }
                text("+ New")
            }
        }
        table("schedule-table") {
            thead {
                tr {
                    th { text("User") }
                    th { text("Email") }
                    th { text("Phone number") }
                    th { text("Role") }
                    th { text("Enabled") }
                    th { text("") }
                }
            }
            tbody {
                for (user in users)
                    tr {
                        td("td-filled") { text("${user.name} ${user.surname}") }
                        td { text(user.email) }
                        td { text(user.phoneNumber) }
                        td { text(user.role) }
                        td { text(if (user.isEnabled) "Yes" else "No") }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
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
    }
}
