package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.frontend.component.main.*
import com.libreguardia.routing.respondHtmlPage
import com.libreguardia.service.ProfessionalFamilyService
import com.libreguardia.util.UUIDSerializer
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
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
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            /*
            get<ProfessionalFamilyAPI.UUID> { professionalFamily ->
                val role = call.principal<UserPrincipal>()?.userRole ?: throw UserNotFoundException()
                val uuid = professionalFamily.uuid
                val professionalFamilyEdit = professionalFamilyService.getThis(
                    uuid = uuid
                ).toProfessionalFamilyEditDTO()
                respondHtmlPage(
                    role = role,
                    content = {
                        professionalFamilyEdit(
                            professionalFamily = professionalFamilyEdit,
                            userUuid = uuid,
                        )
                    }
                )
            }

             */

            get<ProfessionalFamilyAPI> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw UserNotFoundException()
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
            /*
            get<ProfessionalFamilyAPI.New> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw UserNotFoundException()
                respondHtmlPage(
                    role = role,
                    content = { userCreate() }
                )
            }

            post<ProfessionalFamilyAPI> {
                val userCreate = call.receiveParameters().toUserCreateDTO()
                val operationResult = professionalFamilyService.create(
                    userCreateDTO = userCreate
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            userCreate(
                                user = userCreate,
                                errors = operationResult.errors
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/user")
                    }
                }
            }

            patch<ProfessionalFamilyAPI.UUID> { user ->
                val userEdit = call.receiveParameters().toUserEditDTO()
                val operationResult = professionalFamilyService.editThis(
                    userUuid = user.uuid,
                    userEditDTO = userEdit
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            userEdit(
                                user = userEdit,
                                errors = operationResult.errors,
                                userUuid = user.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        //I would prefer to only return the htmx fragment and replace it, but I don't
                        // know if that could be convenient in this case.
                        call.response.headers.append("HX-Redirect", "/user")
                    }
                }
            }

            delete<ProfessionalFamilyAPI.UUID> { professionalFamily ->
                professionalFamily.deleteThis(
                    uuid = professionalFamily.uuid
                )
                call.response.headers.append("HX-Redirect", "/user")
                call.respond(HttpStatusCode.NoContent)
            }

             */
        }
    }
}