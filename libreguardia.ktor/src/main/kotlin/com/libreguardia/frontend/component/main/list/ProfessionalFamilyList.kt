package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.ProfessionalFamilyModel
import io.ktor.htmx.html.*
import io.ktor.utils.io.*
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.professionalFamilyList(
    professionalFamilies: List<ProfessionalFamilyModel>
) {

    button {
        attributes.hx {
            get = "/professional-family/new"
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
        for (professionalFamily in professionalFamilies)
            tr {
                td("td-filled") { text(professionalFamily.name) }
                td {
                    button {
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