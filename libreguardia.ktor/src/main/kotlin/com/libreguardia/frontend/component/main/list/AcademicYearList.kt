package com.libreguardia.frontend.component.main.list

import com.libreguardia.dto.module.AcademicYearResponseDTO
import com.libreguardia.model.AcademicYearModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.academicYearList(
    academicYears: List<AcademicYearModel>
) {
    button {
        attributes.hx {
            get = "/academic-year/new"
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
            th {
                text("Start Date")
            }
            th {
                text("End Date")
            }
        }
        for (academicYear in academicYears)
            tr {
                td("td-filled") { text(academicYear.name) }
                td("td-filled") { text(academicYear.startDate.toString()) }
                td("td-filled") { text(academicYear.endDate.toString()) }
                td {
                    button {
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
