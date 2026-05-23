package com.libreguardia.module.zone

import com.libreguardia.module.zone.ZoneService
import com.libreguardia.util.UUIDSerializer
import io.ktor.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Resource("/zone")
class ZoneAPI {
    @Serializable
    @Resource("new")
    class New(val parent: ZoneAPI = ZoneAPI())

    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: ZoneAPI = ZoneAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: UUID
    )
}

fun Route.zoneRouting(
    service: ZoneService
) {
    /*
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<ZoneAPI> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw ZoneNotFoundException()
                val zones = service.getAll()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        zoneList(
                            zones = zones
                        )
                    }
                )
            }

            get<ZoneAPI.New> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw ZoneNotFoundException()
                respondHtmlPage(
                    role = userRole,
                    content = { zoneCreate() }
                )
            }

            get<ZoneAPI.ByUUID> { zone ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw ZoneNotFoundException()
                val zoneDto = service.getByUUID(zone.uuid)
                respondHtmlPage(
                    role = userRole,
                    content = {
                        zoneEdit(
                            zone = zoneDto.toZoneEditDTO(),
                            zoneUuid = zone.uuid
                        )
                    }
                )
            }

            post<ZoneAPI> {
                val zoneCreate = call.receiveParameters().toZoneCreateDTO()
                val operationResult = service.create(zoneCreate)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            zoneCreate(
                                zone = zoneCreate,
                                errors = operationResult.errors
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/zone")
                    }
                }
            }

            patch<ZoneAPI.ByUUID> { zone ->
                val zoneEdit = call.receiveParameters().toZoneEditDTO()
                val operationResult = service.update(zone.uuid, zoneEdit)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            zoneEdit(
                                zone = zoneEdit,
                                errors = operationResult.errors,
                                zoneUuid = zone.uuid
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/zone")
                    }
                }
            }

            delete<ZoneAPI.ByUUID> { zone ->
                service.delete(zone.uuid)
                call.response.headers.append("HX-Redirect", "/zone")
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

     */
}