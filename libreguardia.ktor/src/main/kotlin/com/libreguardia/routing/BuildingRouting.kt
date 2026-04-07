package com.libreguardia.routing

import com.libreguardia.config.BuildingNotFoundException
import com.libreguardia.config.UUIDSerializer
import com.libreguardia.dto.BuildingRequestDTO
import com.libreguardia.service.BuildingService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable
import java.util.UUID

@Resource("/api/buildings")
class BuildingsAPI {
    @Resource("{uuid}")
    class BuildingUUID(
        val parent: BuildingsAPI = BuildingsAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: UUID
    )
}

fun Route.buildingRouting(
    service: BuildingService
) {
    get<BuildingsAPI> {
        val buildings = service.getAllBuildings()
        call.respond(buildings)
    }

    get<BuildingsAPI.BuildingUUID> { param ->
        val building = service.getBuilding(param.uuid)
        call.respond(building)
    }

    post<BuildingsAPI> {
        val request = call.receive<BuildingRequestDTO>()
        service.createBuilding(request)
        call.respond(HttpStatusCode.Created)
    }

    put<BuildingsAPI.BuildingUUID> { param ->
        val request = call.receive<BuildingRequestDTO>()
        service.editBuilding(param.uuid, request)
        call.respond(HttpStatusCode.OK)
    }

    delete<BuildingsAPI.BuildingUUID> { param ->
        service.deleteBuilding(param.uuid)
        call.respond(HttpStatusCode.OK)
    }
}