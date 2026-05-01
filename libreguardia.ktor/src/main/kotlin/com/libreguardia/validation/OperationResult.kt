package com.libreguardia.validation

sealed class OperationResult {
    class Success : OperationResult()

    data class Error(
        val errors: List<String?>,
    ) : OperationResult()
}