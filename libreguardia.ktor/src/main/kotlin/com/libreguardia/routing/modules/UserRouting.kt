package com.libreguardia.routing.modules

import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.service.UserService
import com.libreguardia.util.UUIDSerializer
import com.libreguardia.util.userUuidFromJwt
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable

@Resource("/api/user")
class UserAPI {
    @Resource("edit-profile")
    class EditProfile(val parent: UserAPI = UserAPI())

    @Resource("{uuid}")
    class UUID(
        val parent: UserAPI = UserAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    ) {
        @Resource("edit")
        class Edit(val parent: UUID)

        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: UUID)

        @Resource("delete")
        class Delete(val parent: UUID)
    }
}

fun Route.userRouting(
    userService: UserService
) {
    authenticate {
        authorized(Role.ADMIN) {
            get<UserAPI> {
                val users = userService.getAllUsers()
                call.respond(users)
            }
            post<UserAPI> {
                val user = call.receive<UserCreateDTO>()
                userService.createUser(
                    userCreateDTO = user
                )
                call.respond(HttpStatusCode.Created)
            }
            patch<UserAPI.UUID.Edit> { user ->
                val userEdit = call.receive<UserEditDTO>()
                userService.editUser(
                    userUUID = user.parent.uuid,
                    userEditDTO = userEdit
                )
                call.respond(HttpStatusCode.OK)
            }
            delete<UserAPI.UUID.Delete> { user ->
                userService.deleteUser(
                    userUUID = user.parent.uuid
                )
                call.respond(HttpStatusCode.NoContent)
            }
            patch<UserAPI.UUID.ToggleEnabled> { user ->
                val enableOrDisable = call.receive<Boolean>()
                userService.toggleEnableUser(
                    userUUID = user.parent.uuid,
                    enableOrDisable = enableOrDisable
                )
                call.respond(HttpStatusCode.OK)
            }
        }
    }
    authenticate {
        authorized(Role.USER, Role.ADMIN) {
            get<UserAPI.UUID> { user ->
                val user = userService.getUser(user.uuid)
                call.respond(user)
            }
            patch<UserAPI.EditProfile> {
                val userEditProfile = call.receive<UserEditProfileDTO>()
                val userUuid = call.userUuidFromJwt()
                userService.editUserProfile(
                    userUUID = userUuid,
                    userEditProfileDTO = userEditProfile
                )
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}