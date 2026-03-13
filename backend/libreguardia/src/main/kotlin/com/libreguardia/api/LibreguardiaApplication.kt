package com.libreguardia.api

import com.libreguardia.api.entity.Place
import com.libreguardia.api.entity.Zone
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibreguardiaApplication

fun main(args: Array<String>) {
	runApplication<LibreguardiaApplication>(*args)
}
