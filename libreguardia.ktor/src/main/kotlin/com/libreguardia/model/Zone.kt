package com.libreguardia.model

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object Zone: UUIDTable(){
    //Con UUID Table no hay que especificar la variable de id,
    // ya que el campo se genera automáticamente
    var name = varchar(
        name = "name",
        length = 50
    )
}