package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.BuildingCreateDTO
import com.libreguardia.dto.module.BuildingEditDTO
import com.libreguardia.service.BuildingService
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

@Serializable
@Resource("/api/building")
class BuildingAPI {
    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: BuildingAPI,
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    ) {
        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: ByUUID)
    }
}

fun Route.buildingRouting(service: BuildingService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<BuildingAPI> {
                call.respond(service.getAll())
            }
            post<BuildingAPI> {
                val dto = call.receive<BuildingCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<BuildingAPI.ByUUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<BuildingAPI.ByUUID> {
                val dto = call.receive<BuildingEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<BuildingAPI.ByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
