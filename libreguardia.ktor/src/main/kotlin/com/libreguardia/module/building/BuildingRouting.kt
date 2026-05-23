package com.libreguardia.module.building

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.config.database.Role
import com.libreguardia.frontend.component.main.create.buildingCreate
import com.libreguardia.frontend.component.main.edit.buildingEdit
import com.libreguardia.frontend.component.main.list.buildingList
import com.libreguardia.config.routing.respondHtmlPage
import com.libreguardia.config.util.UUIDSerializer
import com.libreguardia.config.service.OperationResult
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.patch
import io.ktor.server.resources.delete
import kotlinx.serialization.Serializable


//Still to rework
@Resource("/building")
class BuildingAPI {
    @Resource("new")
    class New(val parent: BuildingAPI = BuildingAPI())

    @Resource("{uuid}")
    class UUID(
        val parent: BuildingAPI = BuildingAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    )
}

fun Route.buildingRouting(service: BuildingService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<BuildingAPI> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val buildings = service.getAll()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        buildingList(
                            buildings = buildings
                        )
                    }
                )
            }

            get<BuildingAPI.New> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                respondHtmlPage(
                    role = userRole,
                    content = { buildingCreate() }
                )
            }

            get<BuildingAPI.UUID> { building ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw NotFoundException()
                val buildingDto = service.getByUUID(building.uuid)
                respondHtmlPage(
                    role = userRole,
                    content = {
                        buildingEdit(
                            building = buildingDto.toBuildingEditDTO(),
                            buildingUuid = building.uuid
                        )
                    }
                )
            }

            post<BuildingAPI> {
                val buildingCreateDTO = call.receiveParameters().toBuildingCreateDTO()
                val operationResult = service.create(
                    buildingCreateDTO = buildingCreateDTO
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            buildingCreate(
                                building = buildingCreateDTO,
                                errors = operationResult.errors
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/building")
                    }
                }
            }

            patch<BuildingAPI.UUID> { building ->
                val buildingEditDTO = call.receiveParameters().toBuildingEditDTO()
                val operationResult = service.update(
                    uuid = building.uuid,
                    buildingEditDTO = buildingEditDTO
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            buildingEdit(
                                building = buildingEditDTO,
                                errors = operationResult.errors,
                                buildingUuid = building.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/building")
                    }
                }
            }

            delete<BuildingAPI.UUID> { building ->
                service.delete(building.uuid)
                call.response.headers.append("HX-Redirect", "/building")
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}