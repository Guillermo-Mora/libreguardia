package com.libreguardia.model

import com.libreguardia.db.WeekDay
import com.libreguardia.db.model.PlaceEntity
import com.libreguardia.db.model.ScheduleEntity
import com.libreguardia.db.model.UserEntity
import com.libreguardia.dto.module.UserEditDTO
import com.libreguardia.repository.UserRepository
import java.util.UUID

data class UserModel(
    val id: UUID,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val isEnabled: Boolean,
    val isDeleted: Boolean,
    val role: String
)

fun UserModel.toUserEditDTO(): UserEditDTO =
    UserEditDTO(
        name = this.name,
        surname = this.surname,
        email = this.email,
        phoneNumber = this.phoneNumber,
        password = "",
        isEnabled = this.isEnabled,
        role = this.role
    )

fun UserEntity.toModel()
= UserModel(
    id = this.id.value,
    name = this.name,
    surname = this.surname,
    email = this.email,
    phoneNumber = this.phoneNumber,
    isEnabled = this.isEnabled,
    isDeleted = this.isDeleted,
    role = this.role.toString()
)

data class UserProfileModel(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val role: String,
    val schedules: WeeklySchedules
)

fun UserRepository.entityToProfileModel(
    entity: UserEntity
) = UserProfileModel(
    fullName = "${entity.name} ${entity.surname}",
    email = entity.email,
    phoneNumber = entity.phoneNumber,
    role = entity.role.toString(),
    schedules = scheduleModelsToScheduleTable(entity.schedules.map { scheduleEntityToModel(it) })
)

private fun scheduleEntityToModel(
    entity: ScheduleEntity
) = ScheduleModel(
    weekDay = entity.weekDay,
    startTime = entity.startTime,
    endTime = entity.endTime,
    groupName = entity.group?.let { group -> "${group.course.name} ${group.code}" },
    activity = entity.scheduleActivity.name,
    //Have to replace this by a PlaceModel
    place = placeEntityToModel(entity.place)
)

private fun placeEntityToModel(
    placeEntity: PlaceEntity
) = PlaceModel(
    fullName = "${placeEntity.placeType.name} ${placeEntity.name}",
    building = placeEntity.building?.name,
    floor = placeEntity.floor
)

private fun scheduleModelsToScheduleTable(
    scheduleModels: List<ScheduleModel>
): WeeklySchedules {
    val groupedSchedules = scheduleModels
        .sortedBy { it.startTime }
        .groupBy { it.weekDay }
    return WeeklySchedules(
        monday = groupedSchedules[WeekDay.MONDAY] ?: emptyList(),
        tuesday = groupedSchedules[WeekDay.TUESDAY] ?: emptyList(),
        wednesday = groupedSchedules[WeekDay.WEDNESDAY] ?: emptyList(),
        thursday = groupedSchedules[WeekDay.THURSDAY] ?: emptyList(),
        friday = groupedSchedules[WeekDay.FRIDAY] ?: emptyList(),
        saturday = groupedSchedules[WeekDay.SATURDAY] ?: emptyList(),
        sunday = groupedSchedules[WeekDay.SUNDAY] ?: emptyList()
    )
}