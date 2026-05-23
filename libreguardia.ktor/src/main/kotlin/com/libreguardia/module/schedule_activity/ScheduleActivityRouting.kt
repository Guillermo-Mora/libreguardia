package com.libreguardia.module.schedule_activity

import com.libreguardia.module.schedule_activity.ScheduleActivityService
import com.libreguardia.util.UUIDSerializer
import io.ktor.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Resource("/schedule-activity")
class ScheduleActivityAPI {
    @Serializable
    @Resource("new")
    class New(val parent: ScheduleActivityAPI = ScheduleActivityAPI())

    @Serializable
    @Resource("{uuid}")
    class ByUUID(
        val parent: ScheduleActivityAPI = ScheduleActivityAPI(),
        @Serializable(with = UUIDSerializer::class) val uuid: UUID
    ) {
        @Serializable
        @Resource("toggle-enabled")
        class ToggleEnabled(val parent: ByUUID)
    }
}

fun Route.scheduleActivityRouting(
    service: ScheduleActivityService
) {
    /*
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN) {
            // HTML Pages
            get<ScheduleActivityAPI> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw ScheduleActivityNotFoundException()
                val activities = service.getAll()
                respondHtmlPage(
                    role = userRole,
                    content = {
                        scheduleActivityList(
                            activities = activities
                        )
                    }
                )
            }

            get<ScheduleActivityAPI.New> {
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw ScheduleActivityNotFoundException()
                respondHtmlPage(
                    role = userRole,
                    content = { scheduleActivityCreate() }
                )
            }

            get<ScheduleActivityAPI.ByUUID> { activity ->
                val userRole = call.principal<UserPrincipal>()?.userRole ?: throw ScheduleActivityNotFoundException()
                val activityDto = service.getByUUID(activity.uuid)
                respondHtmlPage(
                    role = userRole,
                    content = {
                        scheduleActivityEdit(
                            activity = activityDto.toScheduleActivityEditDTO(),
                            activityUuid = activity.uuid
                        )
                    }
                )
            }

            post<ScheduleActivityAPI> {
                val activityCreate = call.receiveParameters().toScheduleActivityCreateDTO()
                val operationResult = service.create(activityCreate)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            scheduleActivityCreate(
                                activity = activityCreate,
                                errors = operationResult.errors
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/schedule-activity")
                    }
                }
            }

            patch<ScheduleActivityAPI.ByUUID> { activity ->
                val activityEdit = call.receiveParameters().toScheduleActivityEditDTO()
                val operationResult = service.update(activity.uuid, activityEdit)
                when (operationResult) {
                    is OperationResult.Error -> {
                        call.respondHtmlFragment {
                            scheduleActivityEdit(
                                activity = activityEdit,
                                errors = operationResult.errors,
                                activityUuid = activity.uuid
                            )
                        }
                    }
                    is OperationResult.Success -> {
                        call.response.headers.append("HX-Redirect", "/schedule-activity")
                    }
                }
            }

            delete<ScheduleActivityAPI.ByUUID> { activity ->
                service.delete(activity.uuid)
                call.response.headers.append("HX-Redirect", "/schedule-activity")
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

     */
}

