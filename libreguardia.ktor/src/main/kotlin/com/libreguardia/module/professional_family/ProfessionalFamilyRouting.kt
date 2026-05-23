package com.libreguardia.module.professional_family

import com.libreguardia.module.shared.util.UUIDSerializer
import io.ktor.resources.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable

@Resource("/professional-family")
class ProfessionalFamilyAPI {
    @Resource("new")
    class New(val parent: ProfessionalFamilyAPI = ProfessionalFamilyAPI())

    @Resource("{uuid}")
    class UUID(
        val parent: ProfessionalFamilyAPI = ProfessionalFamilyAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.professionalFamilyRouting(
    professionalFamilyService: ProfessionalFamilyService
) {
    /*
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {

            get<ProfessionalFamilyAPI.UUID> { professionalFamily ->
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val uuid = professionalFamily.uuid
                val professionalFamilyEdit = professionalFamilyService.getThis(
                    uuid = uuid
                ).toProfessionalFamilyEditDTO()
                respondHtmlPage(
                    role = role,
                    content = {
                        professionalFamilyEdit(
                            dto = professionalFamilyEdit,
                            uuid = uuid,
                        )
                    }
                )
            }

            get<ProfessionalFamilyAPI> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val professionalFamilies = professionalFamilyService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        professionalFamilyList(
                            professionalFamilies = professionalFamilies
                        )
                    }
                )
            }

            get<ProfessionalFamilyAPI.New> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                respondHtmlPage(
                    role = role,
                    content = { professionalFamilyCreate() }
                )
            }

            post<ProfessionalFamilyAPI> {
                val professionalFamilyCreate = call.receiveParameters().toProfessionalFamilyCreateDTO()
                val operationResult = professionalFamilyService.create(
                    professionalFamilyCreateDTO = professionalFamilyCreate
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            professionalFamilyCreate(
                                dto = professionalFamilyCreate,
                                errors = operationResult.errors
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        //This works, however the redirection page should be decided by the page, not by this endpoint.
                        // The best solution would be to add a String field in the Success class, to represent the
                        // path to go if the operation is successfully completed, that can be nullable, In case it
                        // has to stay in the same path.
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/professional-family","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }


            patch<ProfessionalFamilyAPI.UUID> { professionalFamily ->
                val professionalFamilyEdit = call.receiveParameters().toProfessionalFamilyEditDTO()
                val operationResult = professionalFamilyService.editThis(
                    uuid = professionalFamily.uuid,
                    professionalFamilyEditDTO = professionalFamilyEdit
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            professionalFamilyEdit(
                                dto = professionalFamilyEdit,
                                errors = operationResult.errors,
                                uuid = professionalFamily.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        //This way I can return the content from another endpoint without issues and without having
                        // to reload the full page again
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/professional-family","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            delete<ProfessionalFamilyAPI.UUID> { professionalFamily ->
                professionalFamilyService.deleteThis(
                    uuid = professionalFamily.uuid
                )
                call.response.headers.append(
                    "HX-Location",
                    """{"path":"/professional-family","target":"#main-content"}"""
                )
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

     */
}