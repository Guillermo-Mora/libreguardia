package com.libreguardia.module.place

import com.libreguardia.module.building.BuildingService
import com.libreguardia.module.place.PlaceService
import com.libreguardia.module.place_type.PlaceTypeService
import com.libreguardia.module.zone.ZoneService
import com.libreguardia.util.UUIDSerializer
import io.ktor.resources.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable

@Resource("/place")
class PlaceAPI {
    @Resource("new")
    class New(val parent: PlaceAPI = PlaceAPI())

    @Resource("{uuid}")
    class UUID(
        val parent: PlaceAPI = PlaceAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.placeRouting(
    placeService: PlaceService,
    buildingService: BuildingService,
    zoneService: ZoneService,
    placeTypeService: PlaceTypeService
) {
    /*
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<PlaceAPI.UUID> { place ->
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val uuid = place.uuid
                val placeEdit = placeService.getThis(
                    uuid = uuid
                ).toPlaceEditDTO()
                val buildings = buildingService.getAll()
                val zones = zoneService.getAll()
                val placeTypes = placeTypeService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        placeEdit(
                            dto = placeEdit,
                            uuid = uuid,
                            buildings = buildings,
                            zones = zones,
                            placeTypes = placeTypes
                        )
                    }
                )
            }

            get<PlaceAPI> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val places = placeService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        placeList(
                            places = places
                        )
                    }
                )
            }

            get<PlaceAPI.New> {
                val role = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val buildings = buildingService.getAll()
                val zones = zoneService.getAll()
                val placeTypes = placeTypeService.getAll()
                respondHtmlPage(
                    role = role,
                    content = {
                        placeCreate(
                            buildings = buildings,
                            zones = zones,
                            placeTypes = placeTypes
                        )
                    }
                )
            }

            post<PlaceAPI> {
                val placeCreate = call.receiveParameters().toPlaceCreateDTO()
                val operationResult = placeService.create(
                    placeCreateDTO = placeCreate
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        val buildings = buildingService.getAll()
                        val zones = zoneService.getAll()
                        val placeTypes = placeTypeService.getAll()
                        call.respondHtmlFragment {
                            placeCreate(
                                dto = placeCreate,
                                errors = operationResult.errors,
                                buildings = buildings,
                                zones = zones,
                                placeTypes = placeTypes
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/place","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }



            patch<PlaceAPI.UUID> { place ->
                val placeEdit = call.receiveParameters().toPlaceEditDTO()
                val operationResult = placeService.editThis(
                    uuid = place.uuid,
                    placeEditDTO = placeEdit
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        val buildings = buildingService.getAll()
                        val zones = zoneService.getAll()
                        val placeTypes = placeTypeService.getAll()
                        call.respondHtmlFragment {
                            placeEdit(
                                dto = placeEdit,
                                errors = operationResult.errors,
                                buildings = buildings,
                                zones = zones,
                                placeTypes = placeTypes,
                                uuid = place.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append(
                            "HX-Location",
                            """{"path":"/place","target":"#main-content"}"""
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }


            delete<PlaceAPI.UUID> { place ->
                placeService.deleteThis(
                    uuid = place.uuid
                )
                call.response.headers.append(
                    "HX-Location",
                    """{"path":"/place","target":"#main-content"}"""
                )
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

     */
}