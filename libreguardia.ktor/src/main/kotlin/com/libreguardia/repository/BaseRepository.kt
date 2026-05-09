package com.libreguardia.repository

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
import java.util.*

//I should use this to implement more generic methods as select, select all, delete, etc.
// It will save me a lot of repeated code
abstract class BaseRepository<T : UUIDTable>(
    protected val table: T
) {
    fun exists(
        uuid: UUID
    ): Boolean =
        table
            .select(table.id)
            .where { table.id eq uuid }
            .limit(1)
            .count() >= 1

    fun deleteThis(
        uuid: UUID
    ): Boolean =
        table.deleteWhere { table.id eq uuid } == 1
}