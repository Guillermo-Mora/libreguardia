package com.libreguardia.config

import com.libreguardia.db.Role
import com.libreguardia.exception.InsufficientPermissionsException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*

class RbacPluginConfiguration (var roles: Set<Role> = emptySet())

val RoleBasedAuthorizationPlugin = createRouteScopedPlugin(
    name = "RbacPlugin",
    createConfiguration = ::RbacPluginConfiguration
) {
    val roles = pluginConfig.roles
    on(AuthenticationChecked) { call ->
        val roleString = call.principal<JWTPrincipal>()
            ?.payload
            ?.getClaim("role")
            ?.asString() ?: throw InsufficientPermissionsException()
        roles.firstOrNull { it.name == roleString } ?: throw InsufficientPermissionsException()
    }
}

fun Route.authorized(
    vararg hasAnyRole: Role,
    build: Route.() -> Unit
) {
    install(RoleBasedAuthorizationPlugin) { roles = hasAnyRole.toSet() }
    build()

}