package com.libreguardia.service

import com.libreguardia.frontend.component.FormField

fun MutableMap<FormField, String?>.containsErrors() = this.any { it.value != null }