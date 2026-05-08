package com.libreguardia.model

import com.libreguardia.db.WeekDay
import com.libreguardia.repository.ScheduleRepository
import kotlinx.datetime.LocalTime

data class ScheduleModel(
    val weekDay: WeekDay,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val groupName: String?,
    val activity: String,
    val place: PlaceScheduleModel
)

data class WeeklySchedules(
    //Mayabe is better to use here nullable lists instead of empty lists, but for now it stays like this
    val monday: List<ScheduleModel>,
    val tuesday: List<ScheduleModel>,
    val wednesday: List<ScheduleModel>,
    val thursday: List<ScheduleModel>,
    val friday: List<ScheduleModel>,
    val saturday: List<ScheduleModel>,
    val sunday: List<ScheduleModel>
)

fun ScheduleRepository.dataToModel(
    weekDay: WeekDay,
    startTime: LocalTime,
    endTime: LocalTime,
    groupName: String?,
    placeName: String,
    activity: String,
    building: String?,
    floor: String?
): ScheduleModel {
    return ScheduleModel(
        weekDay = weekDay,
        startTime = startTime,
        endTime = endTime,
        groupName = groupName,
        activity = activity,
        place = PlaceScheduleModel(
            fullName = placeName,
            building = building,
            floor = floor
        )
    )
}

fun ScheduleRepository.scheduleModelsToScheduleTable(
    scheduleModels: List<ScheduleModel>
): WeeklySchedules {
    val groupedSchedules = scheduleModels
        //.sortedBy { it.startTime }
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