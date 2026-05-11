package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.PlaceTypeModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.placeTypeList(
    placeTypes: List<PlaceTypeModel>
) {
    button {
        attributes.hx {
            get = "/place-type/new"
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
        for (placeType in placeTypes)
            tr {
                td("td-filled") { text(placeType.name) }
                td {
                    button {
                        attributes.hx {
                            get = "/place-type/${placeType.id}"
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