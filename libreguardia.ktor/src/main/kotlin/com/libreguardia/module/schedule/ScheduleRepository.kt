package com.libreguardia.module.schedule

import com.libreguardia.module.place.PlaceScheduleModel
import com.libreguardia.module.building.BuildingTable
import com.libreguardia.module.course.CourseTable
import com.libreguardia.module.group.GroupTable
import com.libreguardia.module.place.PlaceTable
import com.libreguardia.module.place_type.PlaceTypeTable
import com.libreguardia.module.schedule_activity.ScheduleActivityTable
import com.libreguardia.module.shared.repository.BaseRepository
import org.jetbrains.exposed.v1.core.Concat
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
import java.util.UUID

class ScheduleRepository : BaseRepository<ScheduleTable>(ScheduleTable) {
    fun deleteSchedulesByUserUUID(
        userUUID: UUID
    ) {
        ScheduleTable.deleteWhere { ScheduleTable.user eq userUUID }
    }

    //Implemented this function to replace the one that loaded the user profile schedules using entities with
    // eager loading, as it did a lot of queries, and this way is much more optimized.
    fun getUserWeeklySchedules(
        userUUID: UUID
    ): WeeklySchedules {
        val groupNameCon = Concat(" ", CourseTable.name, GroupTable.code)
        val placeNameCon = Concat(" ", PlaceTypeTable.name, PlaceTable.name)

        return ScheduleTable
            .join(
                otherTable = GroupTable,
                joinType = JoinType.LEFT,
                onColumn = ScheduleTable.group,
                otherColumn = GroupTable.id
            )
            .join(
                otherTable = CourseTable,
                joinType = JoinType.LEFT,
                onColumn = GroupTable.course,
                otherColumn = CourseTable.id
            )
            .join(
                otherTable = PlaceTable,
                joinType = JoinType.LEFT,
                onColumn = ScheduleTable.place,
                otherColumn = PlaceTable.id
            )
            .join(
                otherTable = PlaceTypeTable,
                joinType = JoinType.LEFT,
                onColumn = PlaceTable.placeType,
                otherColumn = PlaceTypeTable.id
            )
            .join(
                otherTable = BuildingTable,
                joinType = JoinType.LEFT,
                onColumn = PlaceTable.building,
                otherColumn = BuildingTable.id
            )
            .join(
                otherTable = ScheduleActivityTable,
                joinType = JoinType.LEFT,
                onColumn = ScheduleTable.scheduleActivity,
                otherColumn = ScheduleActivityTable.id
            )
            .select(
                ScheduleTable.weekDay,
                ScheduleTable.startTime,
                ScheduleTable.endTime,
                groupNameCon,
                placeNameCon,
                BuildingTable.name,
                PlaceTable.floor,
                ScheduleActivityTable.name
            )
            .where { ScheduleTable.user eq userUUID }
            .orderBy(ScheduleTable.startTime)
            .map {
                ScheduleModel(
                    weekDay = it[ScheduleTable.weekDay],
                    startTime = it[ScheduleTable.startTime],
                    endTime = it[ScheduleTable.endTime],
                    groupName = it[groupNameCon],
                    activity = it[ScheduleActivityTable.name],
                    place = PlaceScheduleModel(
                        fullName = it[placeNameCon],
                        building = it[BuildingTable.name],
                        floor = it[PlaceTable.floor]
                    )
                )
            }
            .let(::scheduleModelsToScheduleTable)
    }
}