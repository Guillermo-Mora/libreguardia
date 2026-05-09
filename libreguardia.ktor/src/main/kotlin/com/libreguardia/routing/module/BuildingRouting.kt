package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.toBuildingCreateDTO
import com.libreguardia.dto.module.toBuildingEditDTO
import com.libreguardia.exception.BuildingNotFoundException
import com.libreguardia.frontend.component.main.create.buildingCreate
import com.libreguardia.frontend.component.main.edit.buildingEdit
import com.libreguardia.frontend.component.main.list.buildingList
import com.libreguardia.routing.respondHtmlPage
import com.libreguardia.service.BuildingService
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
@Resource("/building")
class BuildingAPI {
    @Serializable
    @Resource("new")
    class New(val parent: BuildingAPI = BuildingAPI())

    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: BuildingAPI = BuildingAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    ) {
        @Serializable
        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: ByUUID)
    }
}

fun Route.buildingRouting(service: BuildingService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            // HTML Pages
            get<BuildingAPI> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw BuildingNotFoundException()
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
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw BuildingNotFoundException()
                respondHtmlPage(
                    role = userRole,
                    content = { buildingCreate() }
                )
            }

            get<BuildingAPI.ByUUID> { building ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw BuildingNotFoundException()
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
                val buildingCreate = call.receiveParameters().toBuildingCreateDTO()
                val operationResult = service.create(buildingCreate)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            buildingCreate(
                                building = buildingCreate,
                                errors = operationResult.errors
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/building")
                    }
                }
            }

            patch<BuildingAPI.ByUUID> { building ->
                val buildingEdit = call.receiveParameters().toBuildingEditDTO()
                val operationResult = service.update(building.uuid, buildingEdit)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            buildingEdit(
                                building = buildingEdit,
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

            delete<BuildingAPI.ByUUID> { building ->
                service.delete(building.uuid)
                call.response.headers.append("HX-Redirect", "/building")
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
