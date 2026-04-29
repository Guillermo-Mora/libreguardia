package com.libreguardia.routing.modules

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.UserSession
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.EditProfileResult
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.dto.toUserEditProfileDTO
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.frontend.component.dashboard
import com.libreguardia.frontend.component.phoneNumberAndPassword
import com.libreguardia.frontend.component.userProfile
import com.libreguardia.frontend.component.userProfileEdit
import com.libreguardia.frontend.page.mainPage
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
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.serialization.Serializable

@Resource("/user")
class UserAPI {
    @Resource("profile")
    class Profile(val parent: UserAPI = UserAPI()) {
        @Resource("edit")
        class Edit(val parent: Profile)
        @Resource("phone-number")
        class PhoneNumber(val parent: Profile)
    }

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
                val userPrincipal = call.principal<UserPrincipal>() ?: throw UserNotFoundException()
                val userUuid = userPrincipal.userUuid
                val userRole = userPrincipal.userRole
                val userProfileModel = userService.getUserProfile(userUuid = userUuid)
                //If the request header contains that is from HTMX, we send the partial HTML
                // but if it's a normal request to the endpoint, we have to respond with the full page
                if (call.request.headers["HX-Request"] == "true") {
                    call.respondHtmlFragment {
                        userProfile(userProfileModel = userProfileModel)
                    }
                } else {
                    call.respondHtml {
                        mainPage(
                            role = userRole,
                            mainContent = {
                                userProfile(
                                    userProfileModel = userProfileModel
                                )
                            }
                        )
                    }
                }
            }
            get<UserAPI.Profile.Edit> {
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw UserNotFoundException()
                val userPhoneNumber = userService.getUserPhoneNumber(userUuid = userUuid)
                call.respondHtmlFragment {
                    userProfileEdit(phoneNumber = userPhoneNumber)
                }
            }

            get<UserAPI.Profile.PhoneNumber> {
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw UserNotFoundException()
                val userPhoneNumber = userService.getUserPhoneNumber(userUuid = userUuid)
                call.respondHtmlFragment {
                    phoneNumberAndPassword(
                        userPhoneNumber = userPhoneNumber
                    )
                }
            }
            patch<UserAPI.Profile> {
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw UserNotFoundException()
                //TEMPORARY ERROR THROWN, THIS SHOULD BE HANDLED IN A BETTER WAY
                val sessionUuid = call.sessions.get<UserSession>()?.uuid ?: throw UserNotFoundException()
                val userEditProfileDTO = call.receiveParameters().toUserEditProfileDTO()
                val result = userService.editUserProfile(
                    userUuid = userUuid,
                    sessionUuid = sessionUuid,
                    userEditProfileDTO = userEditProfileDTO
                )
                call.respondHtmlFragment {
                    when (result) {
                        is EditProfileResult.Success -> {
                            div {
                                id = "editable-fields"
                                phoneNumberAndPassword(
                                    userPhoneNumber = result.userPhoneNumber
                                )
                            }
                        }

                        is EditProfileResult.Error -> {
                            userProfileEdit(
                                phoneNumber = result.userEditProfileDTO.phoneNumber.toString(),
                                currentPassword = result.userEditProfileDTO.currentPassword.toString(),
                                newPassword = result.userEditProfileDTO.newPassword.toString(),
                                phoneNumberError = result.phoneNumberError,
                                currentPasswordError = result.currentPasswordError,
                                newPasswordError = result.newPasswordError
                            )
                        }
                    }
                }
            }
        }
    }
}