package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.PlaceModel
import io.ktor.htmx.html.*
import io.ktor.utils.io.*
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.placeList(
    places: List<PlaceModel>
) {
    button {
        attributes.hx {
            get = "/place/new"
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
                text("Place")
            }
            th {
                text("Building")
            }
            th {
                text("Floor")
            }
            th {
                text("Zone")
            }
        }
        for (place in places)
            tr {
                td("td-filled") { text("${place.placeTypeName} ${place.name}") }
                td("td-filled") { place.buildingName?.let { text(it) } }
                td("td-filled") { place.floor?.let { text(it) } }
                td("td-filled") { text(place.zoneName) }
                td {
                    button {
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