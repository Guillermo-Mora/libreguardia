package com.libreguardia.util

import com.libreguardia.exception.InvalidCredentialsException
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.util.UUID

suspend fun <T> withTransaction(block: suspend JdbcTransaction.() -> T): T = withContext(Dispatchers.IO) {
    suspendTransaction { block() }
}

fun ApplicationCall.userUuidFromJwt(): UUID {
    return principal<JWTPrincipal>()
        ?.getClaim("uuid", String::class)
        ?.let(UUID::fromString)
        ?: throw InvalidCredentialsException()
}