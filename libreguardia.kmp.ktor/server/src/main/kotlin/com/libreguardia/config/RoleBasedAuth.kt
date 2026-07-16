package com.libreguardia.config

/*
import com.libreguardia.module.shared.database.Role
import com.libreguardia.module.shared.exception.InsufficientPermissionsException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

class RbacPluginConfiguration (var roles: Set<Role> = emptySet())

val RoleBasedAuthorizationPlugin = createRouteScopedPlugin(
    name = "RbacPlugin",
    createConfiguration = ::RbacPluginConfiguration
) {
    val roles = pluginConfig.roles
    on(AuthenticationChecked) { call ->
        val role = call.principal<UserPrincipal>()
            ?.userRole ?: throw InsufficientPermissionsException()
        if (role !in roles) throw InsufficientPermissionsException()
    }
}

fun Route.authorized(
    vararg hasAnyRole: Role,
    build: Route.() -> Unit
) {
    install(RoleBasedAuthorizationPlugin) { roles = hasAnyRole.toSet() }
    build()
}

 */