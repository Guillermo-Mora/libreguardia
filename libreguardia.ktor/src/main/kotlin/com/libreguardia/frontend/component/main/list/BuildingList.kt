package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.BuildingModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.buildingList(
    buildings: List<BuildingModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Buildings") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/building/new"
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
                    th { text("") }
                }
            }
            tbody {
                for (building in buildings)
                    tr {
                        td("td-filled") { text(building.name) }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
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
    }
}
