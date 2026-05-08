package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.toAcademicYearCreateDTO
import com.libreguardia.dto.module.toAcademicYearEditDTO
import com.libreguardia.frontend.component.main.create.academicYearCreate
import com.libreguardia.frontend.component.main.edit.academicYearEdit
import com.libreguardia.frontend.component.main.list.academicYearList
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
class AcademicYearAPI {
    @Serializable
    @Resource("new")
    class New(val parent: AcademicYearAPI = AcademicYearAPI())

    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: AcademicYearAPI = AcademicYearAPI(),
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
            get<AcademicYearAPI> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
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

            get<AcademicYearAPI.New> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
                respondHtmlPage(
                    role = userRole,
                    content = { academicYearCreate() }
                )
            }

            get<AcademicYearAPI.ByUUID> { academicYear ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
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

            post<AcademicYearAPI> {
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

            patch<AcademicYearAPI.ByUUID> { academicYear ->
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

            delete<AcademicYearAPI.ByUUID> { academicYear ->
                service.delete(academicYear.uuid)
                call.response.headers.append("HX-Redirect", "/academic-year")
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
