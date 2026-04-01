package com.libreguardia.routing

import com.libreguardia.dto.AcademicYearRequestDTO
import com.libreguardia.exception.AcademicYearNotFoundException
import com.libreguardia.service.AcademicYearService
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

@Resource("/api/academic-years")
class AcademicYears {
    @Resource("{id}")
    class Id(val parent: AcademicYears = AcademicYears(), val id: String)
}

fun Route.academicYearRouting(
    service: AcademicYearService
) {
    get<AcademicYears> { _ ->
        val years = service.getAll()
        call.respond(years)
    }

    post<AcademicYears> { _ ->
        val request = call.receive<AcademicYearRequestDTO>()
        val errors = request.validate()
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return@post
        }
        val created = service.create(request)
        call.respond(HttpStatusCode.Created, created)
    }

    get<AcademicYears.Id> { params ->
        val id = UUID.fromString(params.id)
        val year = service.getById(id)
        if (year != null) {
            call.respond(year)
        } else {
            throw AcademicYearNotFoundException()
        }
    }

    put<AcademicYears.Id> { params ->
        val id = UUID.fromString(params.id)
        val request = call.receive<AcademicYearRequestDTO>()
        val errors = request.validate()
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return@put
        }
        val updated = service.update(id, request)
        if (updated != null) {
            call.respond(updated)
        } else {
            throw AcademicYearNotFoundException()
        }
    }

    delete("{id}") {
        val id = UUID.fromString(call.parameters["id"])
        val deleted = service.delete(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            throw AcademicYearNotFoundException()
        }
    }
}
