package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.toPlaceTypeCreateDTO
import com.libreguardia.dto.module.toPlaceTypeEditDTO
import com.libreguardia.exception.PlaceTypeNotFoundException
import com.libreguardia.frontend.component.main.create.placeTypeCreate
import com.libreguardia.frontend.component.main.edit.placeTypeEdit
import com.libreguardia.frontend.component.main.list.placeTypeList
import com.libreguardia.routing.respondHtmlPage
import com.libreguardia.service.PlaceTypeService
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
@Resource("/place-type")
class PlaceTypeAPI {
    @Serializable
    @Resource("new")
    class New(val parent: PlaceTypeAPI = PlaceTypeAPI())

    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: PlaceTypeAPI = PlaceTypeAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.placeTypeRouting(service: PlaceTypeService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<PlaceTypeAPI> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw PlaceTypeNotFoundException()
                val placeTypes = service.getAll()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        placeTypeList(placeTypes = placeTypes)
                    }
                )
            }

            get<PlaceTypeAPI.New> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw PlaceTypeNotFoundException()
                respondHtmlPage(
                    role = userRole,
                    content = { placeTypeCreate() }
                )
            }

            get<PlaceTypeAPI.ByUUID> { resource ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw PlaceTypeNotFoundException()
                val placeTypeDto = service.getByUUID(resource.uuid)
                respondHtmlPage(
                    role = userRole,
                    content = {
                        placeTypeEdit(
                            placeType = placeTypeDto.toPlaceTypeEditDTO(),
                            placeTypeUuid = resource.uuid
                        )
                    }
                )
            }

            post<PlaceTypeAPI> {
                val placeTypeCreate = call.receiveParameters().toPlaceTypeCreateDTO()
                val operationResult = service.create(placeTypeCreate)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            placeTypeCreate(
                                placeType = placeTypeCreate,
                                errors = operationResult.errors
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/place-type")
                    }
                }
            }

            patch<PlaceTypeAPI.ByUUID> { resource ->
                val placeTypeEdit = call.receiveParameters().toPlaceTypeEditDTO()
                val operationResult = service.update(resource.uuid, placeTypeEdit)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            placeTypeEdit(
                                placeType = placeTypeEdit,
                                errors = operationResult.errors,
                                placeTypeUuid = resource.uuid
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/place-type")
                    }
                }
            }

            delete<PlaceTypeAPI.ByUUID> { resource ->
                service.delete(resource.uuid)
                call.response.headers.append("HX-Redirect", "/place-type")
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}