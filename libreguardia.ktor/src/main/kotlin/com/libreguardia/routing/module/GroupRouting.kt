package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.toGroupCreateDTO
import com.libreguardia.dto.module.toGroupEditDTO
import com.libreguardia.frontend.component.main.create.courseCreate
import com.libreguardia.frontend.component.main.create.groupCreate
import com.libreguardia.frontend.component.main.edit.courseEdit
import com.libreguardia.frontend.component.main.edit.groupEdit
import com.libreguardia.frontend.component.main.list.groupList
import com.libreguardia.routing.respondHtmlPage
import com.libreguardia.service.CourseService
import com.libreguardia.service.GroupService
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

@Resource("/group")
class GroupAPI {
    @Resource("new")
    class New(val parent: GroupAPI = GroupAPI())

    @Resource("{uuid}")
    class UUID(
        val parent: GroupAPI = GroupAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.groupRouting(
    groupService: GroupService,
    courseService: CourseService,
) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {

            get<GroupAPI.UUID> { group ->
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val uuid = group.uuid
                val groupEdit = groupService.getThis(
                    uuid = uuid
                ).toGroupEditDTO()
                val courses = courseService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        groupEdit(
                            dto = groupEdit,
                            uuid = uuid,
                            courses = courses,
                        )
                    }
                )
            }

            get<GroupAPI> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val groups = groupService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        groupList(
                            groups = groups
                        )
                    }
                )
            }

            get<GroupAPI.New> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val courses = courseService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        groupCreate(
                            courses = courses,
                        )
                    }
                )
            }

            post<GroupAPI> {
                val groupCreate = call.receiveParameters().toGroupCreateDTO()
                val operationResult = groupService.create(
                    groupCreateDTO = groupCreate
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        val courses = courseService.getAll()
                        call.respondHtmlFragment {
                            groupCreate(
                                dto = groupCreate,
                                errors = operationResult.errors,
                                courses = courses
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/group","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }


            patch<GroupAPI.UUID> { group ->
                val groupEdit = call.receiveParameters().toGroupEditDTO()
                val operationResult = groupService.editThis(
                    uuid = group.uuid,
                    groupEditDTO = groupEdit
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        val courses = courseService.getAll()
                        call.respondHtmlFragment {
                            groupEdit(
                                dto = groupEdit,
                                errors = operationResult.errors,
                                courses = courses,
                                uuid = group.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/group","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            delete<GroupAPI.UUID> { course ->
                courseService.deleteThis(
                    uuid = course.uuid
                )
                call.response.headers.append(
                    "HX-Location",
                    """{"path":"/group","target":"#main-content"}"""
                )
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}