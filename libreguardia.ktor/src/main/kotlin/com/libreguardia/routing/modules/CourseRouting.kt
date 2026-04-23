package com.libreguardia.routing.modules

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.CourseCreateDTO
import com.libreguardia.dto.CourseEditDTO
import com.libreguardia.service.CourseService
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
@Resource("/api/course")
class CourseAPI {
    @Serializable
    @Resource("{uuid}")
    class UUID(val parent: CourseAPI, @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID) {
        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: UUID)
    }
}

fun Route.courseRouting(service: CourseService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<CourseAPI> {
                call.respond(service.getAll())
            }
            post<CourseAPI> {
                val dto = call.receive<CourseCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<CourseAPI.UUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<CourseAPI.UUID> {
                val dto = call.receive<CourseEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<CourseAPI.UUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
            patch<CourseAPI.UUID.ToggleEnabled> {
                val enableOrDisable = call.receive<Boolean>()
                service.toggleEnabled(it.parent.uuid, enableOrDisable)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}