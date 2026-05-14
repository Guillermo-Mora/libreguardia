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
        this.id = "editable-fields"
        form("form-card") {
            attributes.hx {
                patch = "/user/profile"
                target = "#editable-fields"
                swap = "outerHTML"
                validate = true
            }
            div("form-card-header") {
                p("form-card-title") { text("Edit profile") }
            }
            div("form-field") {
                label {
                    htmlFor = "phoneNumber"
                    text("Phone number")
                }
                phoneNumberField(
                    phoneNumber = phoneNumber,
                    error = phoneNumberError
                )
            }
            div("form-field") {
                label {
                    htmlFor = "currentPassword"
                    text("Current password")
                }
                currentPasswordField(
                    currentPassword = currentPassword,
                    error = currentPasswordError
                )
            }
            div("form-field") {
                label {
                    htmlFor = "newPassword"
                    text("New password")
                }
                newPasswordField(
                    newPassword = newPassword,
                    error = newPasswordError
                )
            }
            div("form-actions") {
                button {
                    attributes["class"] = "btn btn-primary"
                    type = ButtonType.submit
                    text("Save")
                }
                button {
                    attributes["class"] = "btn btn-ghost"
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
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.phoneNumberField(
    phoneNumber: String,
    error: String? = null
) {
    div {
        this.id = "phone-number"
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
            this.id = "phoneNumber"
            placeholder = "Phone number"
            required = true
        }
        if (error != null)
            p("form-error") { text(error) }
        else
            div("form-error") {}
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.currentPasswordField(
    currentPassword: String,
    error: String? = null
) {
    div {
        this.id = "current-password"
        input {
            value = currentPassword
            type = InputType.password
            name = "currentPassword"
            this.id = "currentPassword"
            placeholder = "Current password"
        }
        if (error != null)
            p("form-error") { text(error) }
        else
            div("form-error") {}
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.newPasswordField(
    newPassword: String,
    error: String? = null
) {
    div {
        this.id = "new-password"
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
            this.id = "newPassword"
            placeholder = "New password"
        }
        if (error != null)
            p("form-error") { text(error) }
        else
            div("form-error") {}
    }
}
