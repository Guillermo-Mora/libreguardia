package com.libreguardia.routing

import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.service.AcademicYearService
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Resource("/api/academic-year")
class AcademicYearResource {
    @Resource("{uuid}")
    class ByUUID(
        val parent: AcademicYearResource,
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    ) {
        @Resource("edit")
        class Edit(val parent: ByUUID)

        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: ByUUID)

        @Resource("delete")
        class Delete(val parent: ByUUID)
    }
}

fun Route.academicYearRouting(service: AcademicYearService) {
    authenticate {
        authorized(Role.ADMIN) {
            get<AcademicYearResource> {
                call.respond(service.getAll())
            }
            post<AcademicYearResource> {
                val dto = call.receive<AcademicYearCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            patch<AcademicYearResource.ByUUID.Edit> { resource ->
                val dto = call.receive<AcademicYearEditDTO>()
                service.update(resource.parent.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<AcademicYearResource.ByUUID.Delete> { resource ->
                service.delete(resource.parent.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
            patch<AcademicYearResource.ByUUID.ToggleEnabled> { resource ->
                val enabled = call.receive<Boolean>()
                service.toggleEnabled(resource.parent.uuid, enabled)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
