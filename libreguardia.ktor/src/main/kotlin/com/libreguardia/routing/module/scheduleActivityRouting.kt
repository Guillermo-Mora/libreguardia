package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.ScheduleActivityCreateDTO
import com.libreguardia.dto.module.ScheduleActivityEditDTO
import com.libreguardia.service.ScheduleActivityService
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
@Resource("/api/schedule-activity")
class ScheduleActivityAPI

@Serializable
@Resource("/api/schedule-activity/{uuid}")
class ScheduleActivityByUUID(
    @Serializable(with = UUIDSerializer::class) val uuid: UUID
) {
    @Resource("toggle-enabled")
    class ToggleEnabled(val parent: ScheduleActivityByUUID)
}

fun Route.scheduleActivityRouting(service: ScheduleActivityService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<ScheduleActivityAPI> {
                call.respond(service.getAll())
            }
            post<ScheduleActivityAPI> {
                val dto = call.receive<ScheduleActivityCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<ScheduleActivityByUUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<ScheduleActivityByUUID> {
                val dto = call.receive<ScheduleActivityEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<ScheduleActivityByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

