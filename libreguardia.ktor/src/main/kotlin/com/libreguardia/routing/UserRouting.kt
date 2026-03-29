package com.libreguardia.routing

import com.libreguardia.dto.UserRequestDTO
import com.libreguardia.service.UserService
import io.ktor.resources.Resource
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

@Resource("/api/users")
class Users {
    @Resource("{email}")
    class Email(val parent: Users = Users(), val email: String) {
        @Resource("edit")
        class Edit(val parent: Email)
    }
}


fun Route.userRouting(
    userService: UserService
) {
    get<Users> {
        val users = userService.getAllUsers()
        call.respond(users)
    }
    post<Users> {
        val user = call.receive<UserRequestDTO>()
    }
    get<Users.Email.Edit> { user ->
        println("Email of the user to edit: ${user.parent.email}")
        //Show a page for editing the user with that email
    }
    put<Users.Email> { user ->
        println("Email of the user to edit: ${user.email}")
        //Edit the user with that email
    }
    delete {

    }
    patch("/byEmail/{userEmail}") {

    }
}