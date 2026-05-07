package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.GroupModel
import io.ktor.htmx.html.*
import io.ktor.utils.io.*
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.groupList(
    groups: List<GroupModel>
) {

    button {
        attributes.hx {
            get = "/group/new"
            replaceUrl = "true"
            pushUrl = "true"
            target = "#main-content"
            swap = "innerHTML"
        }
        text("New")
    }
    table("schedule-table") {
        tr {
            th {
                text("Group")
            }
            th {
                text("Difficulty")
            }
        }
        for (group in groups)
            tr {
                td("td-filled") { text("${group.courseName}-${group.code}") }
                td("td-filled") { text(group.pointsMultiplier) }
                td {
                    button {
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