package com.libreguardia.module.user

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.UserPrincipal
import com.libreguardia.config.UserSession
import com.libreguardia.config.authorized
import com.libreguardia.config.Role
import com.libreguardia.frontend.component.main.*
import com.libreguardia.frontend.component.main.create.userCreate
import com.libreguardia.frontend.component.main.edit.userEdit
import com.libreguardia.frontend.component.main.list.userList
import com.libreguardia.frontend.component.userProfileEdit
import com.libreguardia.config.routing.replaceMainContent
import com.libreguardia.config.routing.respondHtmlPage
import com.libreguardia.util.UUIDSerializer
import com.libreguardia.config.service.OperationResult
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.*
import io.ktor.server.resources.*
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
    )
}

fun Route.userRouting(
    userService: UserService
) {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            get<UserAPI.UUID> { user ->
                val uuid = user.uuid
                val userEditDTO = userService.getUser(
                    userUuid = uuid
                ).toUserEditDTO()
                respondHtmlPage(
                    content = {
                        userEdit(
                            user = userEditDTO,
                            uuid = uuid,
                        )
                    }
                )
            }

            get<UserAPI> {
                val users = userService.getAllUsers()
                respondHtmlPage(
                    content = {
                        userList(
                            users = users
                        )
                    }
                )
            }
            get<UserAPI.New> {
                respondHtmlPage(
                    content = { userCreate() }
                )
            }

            post<UserAPI> {
                val userCreateDTO = call.receiveParameters().toUserCreateDTO()
                val operationResult = userService.createUser(
                    userCreateDTO = userCreateDTO
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            userCreate(
                                user = userCreateDTO,
                                errors = operationResult.errors
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        replaceMainContent(
                            endpoint = "/user",
                            httpStatusCode = HttpStatusCode.OK
                        )
                    }
                }
            }


            patch<UserAPI.UUID> { user ->
                val userEditDTO = call.receiveParameters().toUserEditDTO()
                val operationResult = userService.editUser(
                    uuid = user.uuid,
                    userEditDTO = userEditDTO
                )
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            userEdit(
                                user = userEditDTO,
                                errors = operationResult.errors,
                                uuid = user.uuid
                            )
                        }
                    }

                    is OperationResult.Success -> {
                        replaceMainContent(
                            endpoint = "/user",
                            httpStatusCode = HttpStatusCode.OK
                        )
                    }
                }
            }

            delete<UserAPI.UUID> { user ->
                userService.deleteUser(
                    userUuid = user.uuid
                )
                replaceMainContent(
                    endpoint = "/user",
                    httpStatusCode = HttpStatusCode.NoContent
                )
            }
        }
    }

    //User profile endpoints. They have to be reworked
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN, Role.USER, Role.VISUALIZER) {
            get<UserAPI.Profile> {
                //Temoporary error throw. The errors to throw shouldn't be these.
                val userPrincipal = call.principal<UserPrincipal>() ?: throw NotFoundException()
                val userUuid = userPrincipal.userUuid
                val userProfileModel = userService.getUserProfile(userUuid = userUuid)
                respondHtmlPage(
                    content = {
                        userProfile(
                            userProfileModel = userProfileModel
                        )
                    }
                )
            }
            get<UserAPI.Profile.Edit> {
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw NotFoundException()
                val userPhoneNumber = userService.getUserPhoneNumber(userUuid = userUuid)
                call.respondHtmlFragment {
                    userProfileEdit(phoneNumber = userPhoneNumber)
                }
            }

            get<UserAPI.Profile.PhoneNumber> {
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw NotFoundException()
                val userPhoneNumber = userService.getUserPhoneNumber(userUuid = userUuid)
                call.respondHtmlFragment {
                    phoneNumberAndPassword(
                        userPhoneNumber = userPhoneNumber
                    )
                }
            }
            patch<UserAPI.Profile> {
                val userUuid = call.principal<UserPrincipal>()?.userUuid ?: throw NotFoundException()
                //TEMPORARY ERROR THROWN, THIS SHOULD BE HANDLED IN A BETTER WAY
                val sessionUuid = call.sessions.get<UserSession>()?.uuid ?: throw NotFoundException()
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
                                this.id = "editable-fields"
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