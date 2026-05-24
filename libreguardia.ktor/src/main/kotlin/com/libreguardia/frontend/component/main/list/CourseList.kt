package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.CourseModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.courseList(
    courses: List<CourseModel>
) {
    div("table-container") {
        div("table-header") {
            h2("table-title") { text("Courses") }
            button {
                attributes["class"] = "btn btn-primary btn-sm"
                attributes.hx {
                    get = "/course/new"
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
                    th { text("Professional Family") }
                    th { text("") }
                }
            }
            tbody {
                for (course in courses)
                    tr {
                        td("td-filled") { text(course.name) }
                        td { text(course.professionalFamilyName) }
                        td("table-actions") {
                            button {
                                attributes["class"] = "btn btn-ghost btn-sm"
                                attributes.hx {
                                    get = "/course/${course.id}"
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
