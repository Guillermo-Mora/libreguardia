package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.PlaceTypeCreateDTO
import com.libreguardia.dto.module.PlaceTypeEditDTO
import com.libreguardia.service.PlaceTypeService
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.patch
import io.ktor.server.resources.delete
import kotlinx.serialization.Serializable

@Serializable
@Resource("/api/place-type")
class PlaceTypeAPI

@Serializable
@Resource("{uuid}")
class PlaceTypeByUUID(
    val parent: PlaceTypeAPI = PlaceTypeAPI(),
    @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
)

fun Route.placeTypeRouting(service: PlaceTypeService) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<PlaceTypeAPI> {
                call.respond(service.getAll())
            }
            post<PlaceTypeAPI> {
                val dto = call.receive<PlaceTypeCreateDTO>()
                service.create(dto)
                call.respond(HttpStatusCode.Created)
            }
            get<PlaceTypeByUUID> {
                call.respond(service.getByUUID(it.uuid))
            }
            patch<PlaceTypeByUUID> {
                val dto = call.receive<PlaceTypeEditDTO>()
                service.update(it.uuid, dto)
                call.respond(HttpStatusCode.OK)
            }
            delete<PlaceTypeByUUID> {
                service.delete(it.uuid)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}