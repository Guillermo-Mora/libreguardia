package com.libreguardia.routing

import com.libreguardia.config.UUIDSerializer
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.service.UserService
import com.libreguardia.validation.userValidation
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable

@Resource("/api/users")
class UsersAPI {
    @Resource("{uuid}")
    class UUID(
        val parent: UsersAPI = UsersAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: java.util.UUID
    ) {
        @Resource("edit")
        class Edit(val parent: UUID)

        @Resource("delete")
        class Delete(val parent: UUID)
    }
}

fun Route.userRouting(
    userService: UserService
) {
    userValidation()

    get<UsersAPI> {
        val users = userService.getAllUsers()
        call.respond(users)
    }
    post<UsersAPI> {
        val user = call.receive<UserCreateDTO>()
        userService.createUser(user)
        call.respond(HttpStatusCode.Created)
    }
    get<UsersAPI.UUID> { user ->
        val user = userService.getUser(user.uuid)
        call.respond(user)
    }
    patch<UsersAPI.UUID.Edit> { user ->
        val userEdit = call.receive<UserEditDTO>()
        userService.editUser(
            userUUID = user.parent.uuid,
            userEditDTO = userEdit
        )
    }
    delete<UsersAPI.UUID.Delete> { user ->

    }
}