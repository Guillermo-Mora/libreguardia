package com.libreguardia.model

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ServiceTbl: UUIDTable("service_tbl") {
    var is_signed = bool("is_signed").default(false)
    var absenceId = reference("absence_id", AbsenceTbl.id)
    var userId = reference("user_id", UserTbl.id)
}