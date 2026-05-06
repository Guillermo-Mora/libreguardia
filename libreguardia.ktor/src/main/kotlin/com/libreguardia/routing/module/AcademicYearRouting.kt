package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.AcademicYearCreateDTO
import com.libreguardia.dto.module.AcademicYearEditDTO
import com.libreguardia.dto.module.toAcademicYearCreateDTO
import com.libreguardia.dto.module.toAcademicYearEditDTO
import com.libreguardia.frontend.component.main.*
import com.libreguardia.routing.respondHtmlPage
import com.libreguardia.service.AcademicYearService
import com.libreguardia.util.UUIDSerializer
import com.libreguardia.validation.OperationResult
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.patch
import io.ktor.server.resources.delete
import kotlinx.serialization.Serializable

@Serializable
@Resource("/academic-year")
class AcademicYearPagesAPI {
    @Serializable
    @Resource("new")
    class New(val parent: AcademicYearPagesAPI = AcademicYearPagesAPI())

    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: AcademicYearPagesAPI = AcademicYearPagesAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

@Serializable
@Resource("/api/academic-year")
class AcademicYearAPI {
    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: AcademicYearAPI,
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    ) {
        @Serializable
        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: ByUUID)
    }
}

fun Route.academicYearRouting(service: AcademicYearService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            // HTML Pages
            get<AcademicYearPagesAPI> {
                val userRole = call.principal<com.libreguardia.config.UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
                val academicYears = service.getAll()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        academicYearList(
                            academicYears = academicYears
                        )
                    }
                )
            }

            get<AcademicYearPagesAPI.New> {
                val userRole = call.principal<com.libreguardia.config.UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
                respondHtmlPage(
                    role = userRole,
                    content = { academicYearCreate() }
                )
            }

            get<AcademicYearPagesAPI.ByUUID> { academicYear ->
                val userRole = call.principal<com.libreguardia.config.UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
                val academicYearDto = service.getByUUID(academicYear.uuid)
                respondHtmlPage(
                    role = userRole,
                    content = {
                        academicYearEdit(
                            academicYear = academicYearDto.toAcademicYearEditDTO(),
                            academicYearUuid = academicYear.uuid
                        )
                    }
                )
            }

            post<AcademicYearPagesAPI> {
                val academicYearCreate = call.receiveParameters().toAcademicYearCreateDTO()
                val operationResult = service.create(academicYearCreate)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            academicYearCreate(
                                academicYear = academicYearCreate,
                                errors = operationResult.errors
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/academic-year")
                    }
                }
            }

            patch<AcademicYearPagesAPI.ByUUID> { academicYear ->
                val academicYearEdit = call.receiveParameters().toAcademicYearEditDTO()
                val operationResult = service.update(academicYear.uuid, academicYearEdit)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            academicYearEdit(
                                academicYear = academicYearEdit,
                                errors = operationResult.errors,
                                academicYearUuid = academicYear.uuid
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/academic-year")
                    }
                }
            }

            delete<AcademicYearPagesAPI.ByUUID> { academicYear ->
                service.delete(academicYear.uuid)
                call.response.headers.append("HX-Redirect", "/academic-year")
                call.respond(HttpStatusCode.NoContent)
            }

            // API Routes
            get<AcademicYearAPI> {
                call.respond(service.getAll())
            }
            post<AcademicYearAPI> {
                val dto = call.receive<AcademicYearCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<AcademicYearAPI.ByUUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<AcademicYearAPI.ByUUID> {
                val dto = call.receive<AcademicYearEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<AcademicYearAPI.ByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

fun com.libreguardia.dto.module.AcademicYearResponseDTO.toAcademicYearEditDTO(): com.libreguardia.dto.module.AcademicYearEditDTO =
    com.libreguardia.dto.module.AcademicYearEditDTO(
        name = this.name,
        startDate = this.startDate,
        endDate = this.endDate
    )
