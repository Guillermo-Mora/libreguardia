package com.libreguardia.module.academic_year

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.module.shared.database.Role
import com.libreguardia.frontend.component.main.create.academicYearCreate
import com.libreguardia.frontend.component.main.edit.academicYearEdit
import com.libreguardia.frontend.component.main.list.academicYearList
import com.libreguardia.module.shared.routing.replaceMainContent
import com.libreguardia.module.shared.routing.respondHtmlPage
import com.libreguardia.module.shared.util.UUIDSerializer
import com.libreguardia.module.shared.service.OperationResult
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.patch
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Resource("/academic-year")
class AcademicYearAPI {
    @Resource("new")
    class New(val parent: AcademicYearAPI = AcademicYearAPI())

    @Resource("{uuid}")
    class UUID(
        val parent: AcademicYearAPI = AcademicYearAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.academicYearRouting(academicYearService: AcademicYearService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<AcademicYearAPI> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
                val academicYears = academicYearService.getAll()
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

            get<AcademicYearAPI.UUID> { academicYear ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw IllegalArgumentException()
                val academicYearEditDTO = academicYearService.getByUUID(
                    uuid = academicYear.uuid
                ).toAcademicYearEditDTO()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        academicYearEdit(
                            academicYear = academicYearEditDTO,
                            academicYearUuid = academicYear.uuid
                        )
                    }
                )
            }

            post<AcademicYearAPI> {
                val academicYearCreateDTO = call.receiveParameters().toAcademicYearCreateDTO()
                val operationResult = academicYearService.create(
                    academicYearCreateDTO = academicYearCreateDTO
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            academicYearCreate(
                                academicYear = academicYearCreateDTO,
                                errors = operationResult.errors
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        replaceMainContent(
                            endpoint = "/academic-year",
                            httpStatusCode = HttpStatusCode.OK
                        )
                    }
                }
            }

            patch<AcademicYearAPI.UUID> { academicYear ->
                val academicYearEdit = call.receiveParameters().toAcademicYearEditDTO()
                val operationResult = academicYearService.update(
                    uuid = academicYear.uuid,
                    academicYearEditDTO = academicYearEdit
                )
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
                        replaceMainContent(
                            endpoint = "/academic-year",
                            httpStatusCode = HttpStatusCode.OK
                        )
                    }
                }
            }

            delete<AcademicYearAPI.UUID> { academicYear ->
                academicYearService.delete(academicYear.uuid)
                replaceMainContent(
                    endpoint = "/academic-year",
                    httpStatusCode = HttpStatusCode.NoContent
                )
            }
        }
    }
}