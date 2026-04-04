package com.libreguardia

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import org.testcontainers.postgresql.PostgreSQLContainer

abstract class Testing {
    companion object {
        fun setupTestDBAndFlyway(): DbConnection {
            val testDBContainer = PostgreSQLContainer("postgres:18.3").apply {
                withDatabaseName("testdb")
                withUsername("test")
                withPassword("test")
                start()
            }
            val dbConnection = DbConnection(
                url = testDBContainer.jdbcUrl,
                user = testDBContainer.username,
                password = testDBContainer.password
            )
            Database.connect(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            Flyway.configure().dataSource(
                dbConnection.url,
                dbConnection.user,
                dbConnection.password
            ).load().migrate()
            return dbConnection
        }
    }
}