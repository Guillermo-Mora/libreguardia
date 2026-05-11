package com.libreguardia.frontend.component.main.list

import com.libreguardia.dto.module.ScheduleActivityResponseDTO
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.scheduleActivityList(
    activities: List<ScheduleActivityResponseDTO>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Activities") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/schedule-activity/new"
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
                    th { text("Generates Service") }
                    th { text("") }
                }
            }
            tbody {
                for (activity in activities)
                    tr {
                        td("td-filled") { text(activity.name) }
                        td { text(if (activity.generatesService) "Yes" else "No") }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
                                attributes.hx {
                                    get = "/schedule-activity/${activity.id}"
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
