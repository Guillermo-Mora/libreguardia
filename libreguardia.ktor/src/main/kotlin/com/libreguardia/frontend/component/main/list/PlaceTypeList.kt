package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.PlaceTypeModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.placeTypeList(
    placeTypes: List<PlaceTypeModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Place types") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/place-type/new"
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
                for (placeType in placeTypes)
                    tr {
                        td("td-filled") { text(placeType.name) }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
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
    }
}
