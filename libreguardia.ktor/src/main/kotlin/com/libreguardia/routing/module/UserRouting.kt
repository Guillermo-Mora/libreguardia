package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.UserSession
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.dto.module.EditUserProfileResult
import com.libreguardia.dto.module.toUserCreateDTO
import com.libreguardia.dto.module.toUserEditDTO
import com.libreguardia.dto.module.toUserEditProfileDTO
import com.libreguardia.exception.UserNotFoundException
import com.libreguardia.frontend.component.main.*
import com.libreguardia.frontend.component.userProfileEdit
import com.libreguardia.model.toUserEditDTO
import com.libreguardia.routing.respondHtmlPage
import com.libreguardia.service.UserService
import com.libreguardia.util.UUIDSerializer
import com.libreguardia.validation.OperationResult
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.sessions.*
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.serialization.Serializable

@Resource("/user")
class UserAPI {
    @Resource("new")
    class New(val parent: UserAPI = UserAPI())

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

    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<UserAPI.UUID> { user ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw UserNotFoundException()
                val userUuid = user.uuid
                val userEdit = userService.getUser(
                    userUuid = userUuid
                ).toUserEditDTO()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        userEdit(
                            user = userEdit,
                            userUuid = userUuid,
                        )
                    }
                )
            }

            get<UserAPI> {
                //Temporary error throw.
                // The errors to throw shouldn't be these.
                // The errors to throw, could be HTML fragments that show temporary on screen,
                // showing the error name occurred.
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw UserNotFoundException()
                val users = userService.getAllUsers()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        userList(
                            users = users
                        )
                    }
                )
            }
            get<UserAPI.New> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw UserNotFoundException()
                respondHtmlPage(
                    role = userRole,
                    content = { userCreate() }
                )
            }

            post<UserAPI> {
                val userCreate = call.receiveParameters().toUserCreateDTO()
                val operationResult = userService.createUser(
                    userCreateDTO = userCreate
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            userCreate(
                                user = userCreate,
                                errors = operationResult.errors
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/user")
                    }
                }
            }

            patch<UserAPI.UUID> { user ->
                val userEdit = call.receiveParameters().toUserEditDTO()
                val operationResult = userService.editUser(
                    userUuid = user.uuid,
                    userEditDTO = userEdit
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            userEdit(
                                user = userEdit,
                                errors = operationResult.errors,
                                userUuid = user.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        //I would prefer to only return the htmx fragment and replace it, but I don't
                        // know if that could be convenient in this case.
                        call.response.headers.append("HX-Redirect", "/user")
                    }
                }
            }

            delete<UserAPI.UUID> { user ->
                userService.deleteUser(
                    userUuid = user.uuid
                )
                call.response.headers.append("HX-Redirect", "/user")
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
            get<UserAPI.Profile> {
                //Temoporary error throw. The errors to throw shouldn't be these.
                val userPrincipal = call.principal<UserPrincipal>() ?: throw UserNotFoundException()
                val userUuid = userPrincipal.userUuid
                val userRole = userPrincipal.userRole
                val userProfileModel = userService.getUserProfile(userUuid = userUuid)
                respondHtmlPage(
                    role = userRole,
                    content = {
                        userProfile(
                            userProfileModel = userProfileModel
                        )
                    }
                )
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
                        is EditUserProfileResult.Success -> {
                            div {
                                id = "editable-fields"
                                phoneNumberAndPassword(
                                    userPhoneNumber = result.userPhoneNumber
                                )
                            }
                        }

                        is EditUserProfileResult.Error -> {
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