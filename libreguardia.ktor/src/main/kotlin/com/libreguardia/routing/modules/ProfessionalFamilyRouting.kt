package com.libreguardia.routing.modules

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.ProfessionalFamilyCreateDTO
import com.libreguardia.dto.ProfessionalFamilyEditDTO
import com.libreguardia.service.ProfessionalFamilyService
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
@Resource("/api/professional-family")
class ProfessionalFamilyAPI

@Serializable
@Resource("/api/professional-family/{uuid}")
class ProfessionalFamilyByUUID(
    @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
) {
    @Resource("toggle-enabled")
    class ToggleEnabled(val parent: ProfessionalFamilyByUUID)
}

fun Route.professionalFamilyRouting(service: ProfessionalFamilyService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<ProfessionalFamilyAPI> {
                call.respond(service.getAll())
            }
            post<ProfessionalFamilyAPI> {
                val dto = call.receive<ProfessionalFamilyCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<ProfessionalFamilyByUUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<ProfessionalFamilyByUUID> {
                val dto = call.receive<ProfessionalFamilyEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<ProfessionalFamilyByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
            patch<ProfessionalFamilyByUUID.ToggleEnabled> {
                val enableOrDisable = call.receive<Boolean>()
                service.toggleEnabled(it.parent.uuid, enableOrDisable)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}