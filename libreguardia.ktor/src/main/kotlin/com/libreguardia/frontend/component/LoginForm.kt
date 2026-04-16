package com.libreguardia.frontend.component

import io.ktor.htmx.html.*
import io.ktor.utils.io.*
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.loginForm() {
    form {
        attributes.hx {
            post = "/api/auth/login"
            replaceUrl = "true"
            pushUrl = "true"
        }
        div {
            label {
                htmlFor = "email"
                text("Email") }
            input {
                type = InputType.email
                name = "email"
                id = "email"
                placeholder = "Email"
            }
        }
        div {
            label {
                htmlFor = "password"
                text("Password") }
            input {
                type = InputType.password
                name = "password"
                id = "password"
                placeholder = "Password"
            }
        }
        button {
            type = ButtonType.submit
            text("Submit")
        }
    }
}