package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.GroupModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.groupList(
    groups: List<GroupModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Groups") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/group/new"
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
                    th { text("Name") }
                    th { text("Course") }
                    th { text("") }
                }
            }
            tbody {
                for (group in groups)
                    tr {
                        td("td-filled") { text(group.code) }
                        td { text(group.courseName) }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
                                attributes.hx {
                                    get = "/group/${group.id}"
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
