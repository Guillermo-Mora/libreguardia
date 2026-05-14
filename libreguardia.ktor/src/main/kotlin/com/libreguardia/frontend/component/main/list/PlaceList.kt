package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.PlaceModel
import io.ktor.htmx.html.*
import io.ktor.utils.io.*
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.placeList(
    places: List<PlaceModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Places") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/place/new"
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
                    th { text("Place") }
                    th { text("Building") }
                    th { text("Floor") }
                    th { text("Zone") }
                    th { text("") }
                }
            }
            tbody {
                for (place in places)
                    tr {
                        td("td-filled") { text("${place.placeTypeName} ${place.name}") }
                        td { place.buildingName?.let { text(it) } }
                        td { place.floor?.let { text(it) } }
                        td { text(place.zoneName) }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
                                attributes.hx {
                                    get = "/place/${place.id}"
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
