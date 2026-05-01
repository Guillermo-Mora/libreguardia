package com.libreguardia.routing

import com.libreguardia.db.Role
import com.libreguardia.frontend.page.mainPage
import io.ktor.server.html.respondHtml
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.routing.RoutingContext
import kotlinx.html.FlowContent

//If the request header contains that is from HTMX, we send the partial HTML
// but if it's a normal request to the endpoint, we have to respond with the full page
suspend fun RoutingContext.respondHtmlPage(
    role: Role,
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