package com.libreguardia.config

import com.libreguardia.Testing
import com.libreguardia.db.AbsenceTable
import com.libreguardia.db.AcademicYearTable
import com.libreguardia.db.AppSettingsTable
import com.libreguardia.db.BuildingTable
import com.libreguardia.db.CourseTable
import com.libreguardia.db.GroupTable
import com.libreguardia.db.PlaceTable
import com.libreguardia.db.PlaceTypeTable
import com.libreguardia.db.ProfessionalFamilyTable
import com.libreguardia.db.ScheduleActivityTable
import com.libreguardia.db.ScheduleTable
import com.libreguardia.db.ScheduleTemplateSlotTable
import com.libreguardia.db.ScheduleTemplateTable
import com.libreguardia.db.ServiceTable
import com.libreguardia.db.UserRoleTable
import com.libreguardia.db.UserTable
import com.libreguardia.db.ZoneTable
import io.ktor.server.testing.testApplication
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
                    UserRoleTable,
                    ZoneTable,
                    AppSettingsTable
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