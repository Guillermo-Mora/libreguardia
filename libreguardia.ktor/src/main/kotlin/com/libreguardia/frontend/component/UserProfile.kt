package com.libreguardia.frontend.component

import com.libreguardia.model.UserProfileModel
import io.ktor.htmx.html.hx
import io.ktor.utils.io.*
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.p

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.userProfile(
    userProfileModel: UserProfileModel
) {
    p { text("${userProfileModel.name} ${userProfileModel.surname}") }
    p { text(userProfileModel.email) }
    p { text(userProfileModel.phoneNumber) }
    p { text(userProfileModel.role) }
    p { text("SCHEDULES VISUALIZATION AND PASSWORD MODIFICATION STILL TO IMPLEMENT") }
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