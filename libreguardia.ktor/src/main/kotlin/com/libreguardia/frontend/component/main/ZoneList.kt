package com.libreguardia.frontend.component.main

import com.libreguardia.model.ZoneModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.zoneList(
    zones: List<ZoneModel>
) {
    button {
        attributes.hx {
            get = "/zone/new"
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
        }
        for (zone in zones)
            tr {
                td("td-filled") { text(zone.name) }
                td {
                    button {
                        attributes.hx {
                            get = "/zone/${zone.id}"
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
