package com.libreguardia.frontend.component.main.list

import com.libreguardia.model.CourseModel
import io.ktor.htmx.html.*
import io.ktor.utils.io.*
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.courseList(
    courses: List<CourseModel>
) {

    button {
        attributes.hx {
            get = "/course/new"
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
                text("Professional Family")
            }
        }
        for (course in courses)
            tr {
                td("td-filled") { text(course.name) }
                td("td-filled") { text(course.professionalFamilyName) }
                td {
                    button {
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