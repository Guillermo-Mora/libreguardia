package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.toCourseCreateDTO
import com.libreguardia.dto.module.toCourseEditDTO
import com.libreguardia.frontend.component.main.create.courseCreate
import com.libreguardia.frontend.component.main.edit.courseEdit
import com.libreguardia.frontend.component.main.list.courseList
import com.libreguardia.routing.respondHtmlPage
import com.libreguardia.service.CourseService
import com.libreguardia.service.ProfessionalFamilyService
import com.libreguardia.util.UUIDSerializer
import com.libreguardia.validation.OperationResult
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receiveParameters
import io.ktor.server.resources.*
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable

@Resource("/course")
class CourseAPI {
    @Resource("new")
    class New(val parent: CourseAPI = CourseAPI())

    @Resource("{uuid}")
    class UUID(
        val parent: CourseAPI = CourseAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.courseRouting(
    courseService: CourseService,
    professionalFamilyService: ProfessionalFamilyService
) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {

            get<CourseAPI.UUID> { course ->
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val uuid = course.uuid
                val courseEdit = courseService.getThis(
                    uuid = uuid
                ).toCourseEditDTO()
                val professionalFamilies = professionalFamilyService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        courseEdit(
                            dto = courseEdit,
                            uuid = uuid,
                            professionalFamilies = professionalFamilies,
                        )
                    }
                )
            }

            get<CourseAPI> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val courses = courseService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        courseList(
                            courses = courses
                        )
                    }
                )
            }

            get<CourseAPI.New> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val professionalFamilies = professionalFamilyService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        courseCreate(
                            professionalFamilies = professionalFamilies,
                        )
                    }
                )
            }

            post<CourseAPI> {
                val courseCreate = call.receiveParameters().toCourseCreateDTO()
                val operationResult = courseService.create(
                    courseCreateDTO = courseCreate
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        val professionalFamilies = professionalFamilyService.getAll()
                        call.respondHtmlFragment {
                            courseCreate(
                                dto = courseCreate,
                                errors = operationResult.errors,
                                professionalFamilies = professionalFamilies
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/course","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }


            patch<CourseAPI.UUID> { course ->
                val courseEdit = call.receiveParameters().toCourseEditDTO()
                val operationResult = courseService.editThis(
                    uuid = course.uuid,
                    courseEditDTO = courseEdit
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        val professionalFamilies = professionalFamilyService.getAll()
                        call.respondHtmlFragment {
                            courseEdit(
                                dto = courseEdit,
                                errors = operationResult.errors,
                                professionalFamilies = professionalFamilies,
                                uuid = course.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/course","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            delete<CourseAPI.UUID> { professionalFamily ->
                courseService.deleteThis(
                    uuid = professionalFamily.uuid
                )
                call.response.headers.append(
                    "HX-Location",
                    """{"path":"/course","target":"#main-content"}"""
                )
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}