package com.libreguardia.frontend.component

import kotlinx.html.FlowContent
import kotlinx.html.script

fun FlowContent.htmxScript() {
    script { src = "/static/frontend/js/htmx.min.js" }
}
