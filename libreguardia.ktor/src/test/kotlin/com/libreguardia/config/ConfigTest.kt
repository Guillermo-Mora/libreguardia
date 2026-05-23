package com.libreguardia.config

import com.libreguardia.Testing
import com.libreguardia.module.absence.AbsenceTable
import com.libreguardia.module.academic_year.AcademicYearTable
import com.libreguardia.module.app_settings.AppSettingsTable
import com.libreguardia.module.building.BuildingTable
import com.libreguardia.module.course.CourseTable
import com.libreguardia.module.group.GroupTable
import com.libreguardia.module.place.PlaceTable
import com.libreguardia.module.place_type.PlaceTypeTable
import com.libreguardia.module.professional_family.ProfessionalFamilyTable
import com.libreguardia.module.schedule.ScheduleTable
import com.libreguardia.module.schedule_activity.ScheduleActivityTable
import com.libreguardia.module.schedule_template_slot.ScheduleTemplateSlotTable
import com.libreguardia.module.schedule_template.ScheduleTemplateTable
import com.libreguardia.module.service.ServiceTable
import com.libreguardia.module.user.UserTable
import com.libreguardia.module.zone.ZoneTable
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