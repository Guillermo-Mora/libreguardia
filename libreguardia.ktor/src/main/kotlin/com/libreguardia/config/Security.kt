package com.libreguardia.config

import com.libreguardia.service.JwtService
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt

const val BCRYPT_HASH_COST = 10
fun Application.configureSecurity(
    jwtService: JwtService
) {
    authentication {
        jwt {
            realm = jwtService.realm
            verifier(jwtService.jwtVerifier)

            validate { credential ->
                jwtService.customValidator(credential)
            }
        }

        jwt("another-auth") {
            realm = jwtService.realm
            verifier(jwtService.jwtVerifier)

            validate { credential ->
                jwtService.customValidator(credential)
            }
        }
    }
}