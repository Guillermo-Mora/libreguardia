package com.libreguardia.config.routing

import com.libreguardia.config.Role
import com.libreguardia.config.UserNotFoundException
import com.libreguardia.config.UserPrincipal
import com.libreguardia.frontend.page.mainPage
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.html.respondHtml
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import kotlinx.html.FlowContent

//If the request header contains that is from HTMX, we send the partial HTML
// but if it's a normal request to the endpoint, we have to respond with the full page
suspend fun RoutingContext.respondHtmlPage(
    role: Role = call.principal<UserPrincipal>()?.userRole ?: throw UserNotFoundException(),
    content: FlowContent.() -> Unit
) {
    if (call.request.headers["HX-Request"] == "true") {
        call.respondHtmlFragment {
            content()
        }
    } else {
        call.respondHtml {
            mainPage(
                role = role,
                mainContent = {
                    content()
                }
            )
        }
    }
}

suspend fun RoutingContext.replaceMainContent(
    endpoint: String,
    httpStatusCode: HttpStatusCode
) {
    call.response.headers.append(
        name = "HX-Location",
        value = """{"path":"$endpoint","target":"#main-content"}"""
    )
    call.respond(httpStatusCode)
}