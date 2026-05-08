package com.libreguardia.frontend.component.main

import com.libreguardia.dto.module.BuildingResponseDTO
import com.libreguardia.model.BuildingModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.buildingList(
    buildings: List<BuildingModel>
) {
    button {
        attributes.hx {
            get = "/building/new"
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
        for (building in buildings)
            tr {
                td("td-filled") { text(building.name) }
                td {
                    button {
                        attributes.hx {
                            get = "/building/${building.id}"
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
