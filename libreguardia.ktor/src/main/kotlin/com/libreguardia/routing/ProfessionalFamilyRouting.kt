package com.libreguardia.routing

import com.libreguardia.dto.ProfessionalFamilyRequestDTO
import com.libreguardia.exception.ProfessionalFamilyNotFoundException
import com.libreguardia.service.ProfessionalFamilyService
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import java.util.UUID

@Resource("/api/professional-families")
class ProfessionalFamilies {
    @Resource("{id}")
    class Id(val parent: ProfessionalFamilies = ProfessionalFamilies(), val id: String)
}

fun Route.professionalFamilyRouting(
    service: ProfessionalFamilyService
) {
    get<ProfessionalFamilies> { _ ->
        val families = service.getAll()
        call.respond(families)
    }

    post<ProfessionalFamilies> { _ ->
        val request = call.receive<ProfessionalFamilyRequestDTO>()
        val errors = request.validate()
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return@post
        }
        val created = service.create(request)
        call.respond(HttpStatusCode.Created, created)
    }

    get<ProfessionalFamilies.Id> { params ->
        val id = UUID.fromString(params.id)
        val family = service.getById(id)
        if (family != null) {
            call.respond(family)
        } else {
            throw ProfessionalFamilyNotFoundException()
        }
    }

    put<ProfessionalFamilies.Id> { params ->
        val id = UUID.fromString(params.id)
        val request = call.receive<ProfessionalFamilyRequestDTO>()
        val errors = request.validate()
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return@put
        }
        val updated = service.update(id, request)
        if (updated != null) {
            call.respond(updated)
        } else {
            throw ProfessionalFamilyNotFoundException()
        }
    }

    delete("{id}") {
        val id = UUID.fromString(call.parameters["id"])
        val deleted = service.delete(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            throw ProfessionalFamilyNotFoundException()
        }
    }
}
