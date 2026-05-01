package com.libreguardia.frontend.component

import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.p

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.userProfileEdit(
    phoneNumber: String,
    currentPassword: String = "",
    newPassword: String = "",
    phoneNumberError: String? = null,
    currentPasswordError: String? = null,
    newPasswordError: String? = null
) {
    div {
        id = "editable-fields"
        form {
            attributes.hx {
                patch = "/user/profile"
                target = "#editable-fields"
                swap = "outerHTML"
                validate = true
            }
            div {
                label {
                    htmlFor = "phoneNumber"
                    text("Phone number")
                }
                phoneNumberField(
                    phoneNumber = phoneNumber,
                    error = phoneNumberError
                )
            }
            div {
                label {
                    htmlFor = "currentPassword"
                    text("Current password")
                }
                currentPasswordField(
                    currentPassword = currentPassword,
                    error = currentPasswordError
                )
            }
            div {
                label {
                    htmlFor = "newPassword"
                    text("New password")
                }
                newPasswordField(
                    newPassword = newPassword,
                    error = newPasswordError
                )
            }
            button {
                type = ButtonType.submit
                text("Save")
            }
            button {
                attributes.hx {
                    trigger = "click"
                    get = "/user/profile/phone-number"
                    target = "#editable-fields"
                    swap = "innerHTML"
                }
                text("Cancel")
            }
        }
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.phoneNumberField(
    phoneNumber: String,
    error: String? = null
) {
    div {
        id = "phone-number"
        input {
            attributes.hx {
                post = "/validation/phone-number"
                trigger = "input changed delay:500ms"
                target = "#phone-number"
                swap = "outerHTML"
                sync = "closest form:abort"
            }
            value = phoneNumber
            type = InputType.tel
            name = "phoneNumber"
            id = "phoneNumber"
            placeholder = "Phone number"
            required = true
        }
        if (error != null)
            p { text(error) }
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.currentPasswordField(
    currentPassword: String,
    error: String? = null
) {
    div {
        id = "current-password"
        input {
            value = currentPassword
            type = InputType.password
            name = "currentPassword"
            id = "currentPassword"
            placeholder = "Current password"
        }
        if (error != null)
            p { text(error) }
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.newPasswordField(
    newPassword: String,
    error: String? = null
) {
    div {
        id = "new-password"
        input {
            attributes.hx {
                post = "/validation/new-password"
                trigger = "input changed delay:500ms"
                target = "#new-password"
                swap = "outerHTML"
                sync = "closest form:abort"
            }
            value = newPassword
            type = InputType.password
            name = "newPassword"
            id = "newPassword"
            placeholder = "New password"
        }
        if (error != null)
            p { text(error) }
    }
}