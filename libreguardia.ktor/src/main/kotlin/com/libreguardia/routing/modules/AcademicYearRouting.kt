package com.libreguardia.routing.modules

import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.service.AcademicYearService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.academicYearRouting(service: AcademicYearService) {
    authenticate {
        authorized(Role.ADMIN) {
            route("/api/academic-year") {
                get { call.respond(service.getAll()) }
                post {
                    val dto = call.receive<AcademicYearCreateDTO>()
                    service.create(dto)
                    call.respond(HttpStatusCode.Created)
                }
                get("/{uuid}") {
                    val uuid = java.util.UUID.fromString(call.parameters["uuid"])
                    call.respond(service.getByUUID(uuid))
                }
                patch("/{uuid}") {
                    val uuid = java.util.UUID.fromString(call.parameters["uuid"])
                    val dto = call.receive<AcademicYearEditDTO>()
                    service.update(uuid, dto)
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{uuid}") {
                    val uuid = java.util.UUID.fromString(call.parameters["uuid"])
                    service.delete(uuid)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
