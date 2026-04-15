package com.libreguardia.routing.modules

import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.service.AcademicYearService
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
@Resource("/api/academic-year")
class AcademicYearAPI

@Serializable
@Resource("/api/academic-year/{uuid}")
class AcademicYearByUUID(
    @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
) {
    @Resource("toggle-enabled")
    class ToggleEnabled(val parent: AcademicYearByUUID)
}

fun Route.academicYearRouting(service: AcademicYearService) {
    authenticate {
        authorized(Role.ADMIN) {
            get<AcademicYearAPI> {
                call.respond(service.getAll())
            }
            post<AcademicYearAPI> {
                val dto = call.receive<AcademicYearCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<AcademicYearByUUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<AcademicYearByUUID> {
                val dto = call.receive<AcademicYearEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<AcademicYearByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
            patch<AcademicYearByUUID.ToggleEnabled> {
                val enableOrDisable = call.receive<Boolean>()
                service.toggleEnabled(it.parent.uuid, enableOrDisable)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}