package com.libreguardia.module.shared.service

import com.libreguardia.frontend.component.FormField

sealed class OperationResult {
    class Success : OperationResult()

    data class Error(
        val errors: Map<FormField, String?>,
    ) : OperationResult()
}