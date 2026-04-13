package com.libreguardia.db.model

import com.libreguardia.db.Role
import com.libreguardia.db.WeekDay
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object UserTable: UUIDTable(
    name =  "user_tbl"
) {
    val name = varchar(
        name = "name",
        length = 50
    )
    val surname = varchar(
        name = "surname",
        length = 50
    )
    val email = varchar(
        name = "email",
        length = 50
    ).uniqueIndex()
    val phoneNumber = varchar(
        name = "phone_number",
        length = 20
    )
    val password = varchar(
        name = "password",
        length = 60
    )
    val isEnabled = bool(
        name = "is_enabled"
    ).default(true)
    val isDeleted = bool(
        name = "is_deleted"
    ).default(false)
    val role = enumerationByName<Role>(
        name = "role",
        length = 60
    )
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var name by UserTable.name
    var surname by UserTable.surname
    var email by UserTable.email
    var phoneNumber by UserTable.phoneNumber
    var password by UserTable.password
    var isEnabled by UserTable.isEnabled
    var isDeleted by UserTable.isDeleted
    var role by UserTable.role
    val schedules by ScheduleEntity referrersOn ScheduleTable.user
    val absences by AbsenceEntity optionalReferrersOn AbsenceTable.user
    val servicesCovered by ServiceEntity optionalReferrersOn ServiceTable.coverUser
}