package com.libreguardia.routing.modules

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.ZoneCreateDTO
import com.libreguardia.dto.ZoneEditDTO
import com.libreguardia.service.ZoneService
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
@Resource("/api/zone")
class ZoneAPI {
    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: ZoneAPI,
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.zoneRouting(service: ZoneService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<ZoneAPI> {
                call.respond(service.getAll())
            }
            post<ZoneAPI> {
                val dto = call.receive<ZoneCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            patch<ZoneAPI.ByUUID> {
                val dto = call.receive<ZoneEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<ZoneAPI.ByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}