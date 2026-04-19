package com.libreguardia.routing.modules

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.UserEditProfileDTO
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.frontend.component.userProfile
import com.libreguardia.service.UserService
import com.libreguardia.util.UUIDSerializer
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable

@Resource("/user")
class UserAPI {
    @Resource("profile")
    class Profile(val parent: UserAPI = UserAPI())

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
    //TEMPORARY OUT HERE FOR TESTING
    patch<UserAPI.UUID> { user ->
        val userEdit = call.receive<UserEditDTO>()
        userService.editUser(
            userUuid = user.uuid,
            userEditDTO = userEdit
        )
        call.respond(HttpStatusCode.OK)
    }

    authenticate(AUTH_SESSION) {
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
            /*
            patch<UserAPI.UUID> { user ->
                val userEdit = call.receive<UserEditDTO>()
                userService.editUser(
                    userUuid = user.uuid,
                    userEditDTO = userEdit
                )
                call.respond(HttpStatusCode.OK)
            }
             */
            delete<UserAPI.UUID> { user ->
                userService.deleteUser(
                    userUuid = user.uuid
                )
                call.respond(HttpStatusCode.NoContent)
            }
            patch<UserAPI.UUID.ToggleEnabled> { user ->
                val enableOrDisable = call.receive<Boolean>()
                userService.toggleEnableUser(
                    userUuid = user.parent.uuid,
                    enableOrDisable = enableOrDisable
                )
                call.respond(HttpStatusCode.OK)
            }
        }
    }
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN, Role.USER, Role.VISUALIZER) {
            get<UserAPI.UUID> { user ->
                val user = userService.getUser(user.uuid)
                call.respond(user)
            }
            get<UserAPI.Profile> {
                //Temoporary error throw. The errors to throw shouldn't be these.
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw UserNotFoundException()
                val userProfileModel = userService.getUserProfile(userUuid = userUuid)
                call.respondHtmlFragment {
                    userProfile(userProfileModel = userProfileModel)
                }
            }
            patch<UserAPI.Profile> {
                val userEditProfile = call.receive<UserEditProfileDTO>()
                //val userUuid = call.userUuidFromJwt()
                //userService.editUserProfile(
                //    userUuid = userUuid,
                //    userEditProfileDTO = userEditProfile
                //)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}