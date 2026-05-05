package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.GroupCreateDTO
import com.libreguardia.dto.module.GroupEditDTO
import com.libreguardia.service.GroupService
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.patch
import io.ktor.server.resources.delete
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Resource("/api/group")
class GroupAPI {
    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: GroupAPI,
        @Serializable(with = UUIDSerializer::class) val uuid: UUID
    ) {
        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: ByUUID)
    }
}

fun Route.groupRouting(service: GroupService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<GroupAPI> {
                call.respond(service.getAll())
            }
            post<GroupAPI> {
                val dto = call.receive<GroupCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<GroupAPI.ByUUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<GroupAPI.ByUUID> {
                val dto = call.receive<GroupEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<GroupAPI.ByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
            patch<GroupAPI.ByUUID.ToggleEnabled> {
                val enableOrDisable = call.receive<Boolean>()
                service.toggleEnabled(it.parent.uuid, enableOrDisable)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
