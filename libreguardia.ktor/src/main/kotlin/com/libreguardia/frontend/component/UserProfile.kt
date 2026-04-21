package com.libreguardia.frontend.component

import com.libreguardia.model.ScheduleModel
import com.libreguardia.model.UserProfileModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.*
import kotlinx.html.FlowContent
import kotlinx.html.TR
import kotlinx.html.br
import kotlinx.html.button
import kotlinx.html.caption
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.small
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.userProfile(
    userProfileModel: UserProfileModel
) {
    val mostSchedulesInADay = maxOf(
        userProfileModel.schedules.monday.size,
        userProfileModel.schedules.tuesday.size,
        userProfileModel.schedules.wednesday.size,
        userProfileModel.schedules.thursday.size,
        userProfileModel.schedules.friday.size,
        userProfileModel.schedules.saturday.size,
        userProfileModel.schedules.sunday.size,
    )
    p { text(userProfileModel.fullName) }
    p { text(userProfileModel.email) }
    p { text(userProfileModel.phoneNumber) }
    p { text(userProfileModel.role) }
    p { text("SCHEDULES VISUALIZATION AND PASSWORD AND PHONE NUMBER MODIFICATION STILL TO IMPLEMENT") }
    br
    if (mostSchedulesInADay > 0) {
        table("schedule-table")
        {
            caption {
                text("Your schedule")
            }
            tr {
                th { text("MO") }
                th { text("TU") }
                th { text("WE") }
                th { text("TH") }
                th { text("FR") }
                th { text("SA") }
                th { text("SU") }
            }
            for (index in 0 until mostSchedulesInADay) {
                tr {
                    setTd(
                        weekDaySchedules = userProfileModel.schedules.monday,
                        index = index
                    )
                    setTd(
                        weekDaySchedules = userProfileModel.schedules.tuesday,
                        index = index
                    )
                    setTd(
                        weekDaySchedules = userProfileModel.schedules.wednesday,
                        index = index
                    )
                    setTd(
                        weekDaySchedules = userProfileModel.schedules.thursday,
                        index = index
                    )
                    setTd(
                        weekDaySchedules = userProfileModel.schedules.friday,
                        index = index
                    )
                    setTd(
                        weekDaySchedules = userProfileModel.schedules.saturday,
                        index = index
                    )
                    setTd(
                        weekDaySchedules = userProfileModel.schedules.sunday,
                        index = index
                    )
                }
            }
        }
    }
    br
    button {
        attributes.hx {
            trigger = "click"
            post = "/auth/logout"
        }
        text("Logout")
    }
    button {
        attributes.hx {
            trigger = "click"
            post = "/auth/logout-all-devices"
        }
        text("Logout all devices")
    }
}

private fun TR.setTd(
    weekDaySchedules: List<ScheduleModel>,
    index: Int
) {
    weekDaySchedules.getOrNull(index)?.let { schedule ->
        td("td-filled") {
            div {
                p {
                    small { text("${schedule.startTime} - ${schedule.endTime}") }
                }
                p {
                    small { text(schedule.place.fullName) }
                }
                p {
                    small { text("Activity: ${schedule.activity}") }
                }
                p {
                    schedule.place.building?.let { building -> small { text("Building: $building") } }
                }
                p {
                    schedule.place.floor?.let { floor -> small { text("Floor: $floor") } }
                }
                p {
                    schedule.groupName?.let { group -> small { text(group) } }
                }
            }
        }
    } ?: td { }
}