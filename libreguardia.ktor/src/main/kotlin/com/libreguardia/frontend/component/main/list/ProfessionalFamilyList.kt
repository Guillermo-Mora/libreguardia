package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.ProfessionalFamilyModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.professionalFamilyList(
    professionalFamilies: List<ProfessionalFamilyModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Professional families") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/professional-family/new"
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
                for (professionalFamily in professionalFamilies)
                    tr {
                        td("td-filled") { text(professionalFamily.name) }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
                                attributes.hx {
                                    get = "/professional-family/${professionalFamily.id}"
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
