package com.libreguardia.routing

import com.libreguardia.dto.CourseRequestDTO
import com.libreguardia.exception.CourseNotFoundException
import com.libreguardia.service.CourseService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import java.util.UUID

@Resource("/api/courses")
class CoursesAPI {
    @Resource("{id}")
    class Id(val parent: CoursesAPI = CoursesAPI(), val id: String) {
        @Resource("edit")
        class Edit(val parent: Id)
        @Resource("delete")
        class Delete(val parent: Id)
    }
}

fun Route.courseRouting(
    service: CourseService
) {
    get<CoursesAPI> { _ ->
        val courses = service.getAll()
        call.respond(courses)
    }

    post<CoursesAPI> { _ ->
        val request = call.receive<CourseRequestDTO>()
        val errors = request.validate()
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return@post
        }
        val created = service.create(request)
        call.respond(HttpStatusCode.Created, created)
    }

    get<CoursesAPI.Id> { params ->
        val id = UUID.fromString(params.id)
        val course = service.getById(id)
        if (course != null) {
            call.respond(course)
        } else {
            throw CourseNotFoundException()
        }
    }

    put<CoursesAPI.Id.Edit> { params ->
        val id = UUID.fromString(params.parent.id)
        val request = call.receive<CourseRequestDTO>()
        val errors = request.validate()
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return@put
        }
        val updated = service.update(id, request)
        if (updated != null) {
            call.respond(updated)
        } else {
            throw CourseNotFoundException()
        }
    }

    delete<CoursesAPI.Id.Delete> { params ->
        val id = UUID.fromString(params.parent.id)
        val deleted = service.delete(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            throw CourseNotFoundException()
        }
    }
}