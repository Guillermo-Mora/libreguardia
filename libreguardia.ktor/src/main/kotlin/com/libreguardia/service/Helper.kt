package com.libreguardia.service

import com.libreguardia.frontend.component.FormField

fun containsErrors(
    errors: MutableMap<FormField, String?>
) = errors.any { it.value != null }