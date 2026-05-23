package com.libreguardia.module.place_type

import com.libreguardia.module.shared.util.UUIDSerializer
import io.ktor.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

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
        @Serializable(with = UUIDSerializer::class) val uuid: UUID
    )
}

fun Route.placeTypeRouting(
    service: PlaceTypeService
) {
    /*
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

     */
}