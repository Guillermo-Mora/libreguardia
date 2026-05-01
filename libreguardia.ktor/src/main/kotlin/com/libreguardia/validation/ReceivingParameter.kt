package com.libreguardia.validation

import io.ktor.http.Parameters

fun Parameters.receiveParameter(
    parameterName: String
) = this[parameterName]
