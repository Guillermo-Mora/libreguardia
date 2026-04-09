package com.libreguardia.config

import com.libreguardia.Testing
import com.libreguardia.db.configureDatabase
import com.libreguardia.db.model.AbsenceTable
import com.libreguardia.db.model.AcademicYearTable
import com.libreguardia.db.model.AppSettingsTable
import com.libreguardia.db.model.BuildingTable
import com.libreguardia.db.model.CourseTable
import com.libreguardia.db.model.GroupTable
import com.libreguardia.db.model.PlaceTable
import com.libreguardia.db.model.PlaceTypeTable
import com.libreguardia.db.model.ProfessionalFamilyTable
import com.libreguardia.db.model.RefreshTokenTable
import com.libreguardia.db.model.ScheduleActivityTable
import com.libreguardia.db.model.ScheduleTable
import com.libreguardia.db.model.ScheduleTemplateSlotTable
import com.libreguardia.db.model.ScheduleTemplateTable
import com.libreguardia.db.model.ServiceTable
import com.libreguardia.db.model.UserRoleTable
import com.libreguardia.db.model.UserTable
import com.libreguardia.db.model.ZoneTable
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