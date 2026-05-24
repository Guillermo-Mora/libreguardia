package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.AcademicYearModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.academicYearList(
    academicYears: List<AcademicYearModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Academic years") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/academic-year/new"
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
                    th { text("Start Date") }
                    th { text("End Date") }
                    th { text("") }
                }
            }
            tbody {
                for (academicYear in academicYears)
                    tr {
                        td("td-filled") { text(academicYear.name) }
                        td { text(academicYear.startDate) }
                        td { text(academicYear.endDate) }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
                                attributes.hx {
                                    get = "/academic-year/${academicYear.id}"
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
