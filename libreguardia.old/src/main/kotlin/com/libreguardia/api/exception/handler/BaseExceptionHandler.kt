package com.libreguardia.api.exception.handler

abstract class BaseExceptionHandler {
    protected fun createResponseEntity(message: String) = mapOf("message" to message)
}