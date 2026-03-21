package com.libreguardia.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibreguardiaApplication

fun main(args: Array<String>) {
	runApplication<LibreguardiaApplication>(*args)
}