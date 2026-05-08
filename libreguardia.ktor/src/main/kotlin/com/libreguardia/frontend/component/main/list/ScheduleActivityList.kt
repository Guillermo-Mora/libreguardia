package com.libreguardia.frontend.component.main.list

import com.libreguardia.dto.module.ScheduleActivityResponseDTO
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.scheduleActivityList(
    activities: List<ScheduleActivityResponseDTO>
) {
    button {
        attributes.hx {
            get = "/schedule-activity/new"
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
                text("Name")
            }
            th {
                text("Generates Service")
            }
        }
        for (activity in activities)
            tr {
                td("td-filled") { text(activity.name) }
                td("td-filled") { text(if (activity.generatesService) "Yes" else "No") }
                td {
                    button {
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
