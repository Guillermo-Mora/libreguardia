package com.libreguardia.frontend.page

import com.libreguardia.frontend.component.htmxScript
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

@OptIn(ExperimentalKtorApi::class)
fun HTML.loginPage() {
    head {
        title { text("Libreguardia ~ login") }
        meta { charset = "utf-8" }
        meta { name = "viewport"; content = "width=device-width, initial-scale=1.0" }
        link { rel = "stylesheet"; href = "/static/frontend/css/style.css" }
    }
    body("login-body") {
        div("login-card") {
            div("login-header") {
                h1("login-logo") { text("LibreGuardia") }
                p("login-subtitle") { text("Sign in to your account") }
            }
            form(classes = "login-form") {
                attributes.hx {
                    post = "/auth/login"
                    target = "#password-div"
                    swap = "outerHTML"
                    validate = true
                }
                div("form-field") {
                    label {
                        htmlFor = "email"
                        text("Email")
                    }
                    input {
                        attributes.hx { validate = true }
                        type = InputType.email
                        name = "email"
                        this.id = "email"
                        placeholder = "you@example.com"
                        required = true
                    }
                }
                div("form-field") {
                    this.id = "password-div"
                    label {
                        htmlFor = "password"
                        text("Password")
                    }
                    input {
                        attributes.hx { validate = true }
                        type = InputType.password
                        name = "password"
                        this.id = "password"
                        placeholder = "Enter your password"
                        required = true
                    }
                    div("form-error") {}
                }
                button {
                    attributes["class"] = "btn btn-primary btn-lg btn-full"
                    type = ButtonType.submit
                    text("Sign in")
                }
            }
            p("login-footer") {
                text("LibreGuardia v1.0")
            }
        }
        htmxScript()
    }
}
