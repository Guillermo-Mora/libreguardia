package com.libreguardia.config

import com.libreguardia.Testing
import com.libreguardia.db.configureDatabase
import com.libreguardia.db.model.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.testcontainers.DockerClientFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConfigTest {
    @Test
    fun exposedTablesMatchToDatabase() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()

        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
        }
        var missingColStatements = listOf<String>()
        transaction {
            missingColStatements =
                SchemaUtils.addMissingColumnsStatements(
                    AbsenceTable,
                    AcademicYearTable,
                    BuildingTable,
                    CourseTable,
                    GroupTable,
                    PlaceTable,
                    PlaceTypeTable,
                    ProfessionalFamilyTable,
                    ScheduleActivityTable,
                    ScheduleTable,
                    ScheduleTemplateTable,
                    ScheduleTemplateSlotTable,
                    ServiceTable,
                    UserTable,
                    ZoneTable,
                    AppSettingsTable,
                    RefreshTokenTable
                )
        }
        missingColStatements.forEach { println(it) }
        assertEquals(
            expected = emptyList(),
            actual = missingColStatements
        )
    }

    @Test
    fun dockerIsAccessible() = assertTrue(DockerClientFactory.instance().isDockerAvailable)
}