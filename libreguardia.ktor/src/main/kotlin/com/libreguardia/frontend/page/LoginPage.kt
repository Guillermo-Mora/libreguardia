package com.libreguardia.frontend.page

import com.libreguardia.frontend.component.*
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
    body {
        main("login-main")
        {
            form(classes = "login-form") {
                attributes.hx {
                    post = "/auth/login"
                    target = "#password-div"
                    swap = "outerHTML"
                    validate = true
                }
                div {
                    h2 {
                        text("Login")
                    }
                }
                div("input-div") {
                    label {
                        htmlFor = "email"
                        text("Email")
                    }
                    input {
                        attributes.hx {
                            validate = true
                        }
                        type = InputType.email
                        name = "email"
                        id = "email"
                        placeholder = "Input your email"
                        required = true
                    }
                }
                div("input-div") {
                    id = "password-div"
                    label {
                        htmlFor = "password"
                        text("Password")
                    }
                    input {
                        attributes.hx {
                            validate = true
                        }
                        type = InputType.password
                        name = "password"
                        id = "password"
                        placeholder = "Input your password"
                        required = true
                    }
                    div("error-div") {}
                }
                button {
                    type = ButtonType.submit
                    text("Login")
                }
            }
        }
        htmxScript()
    }
}