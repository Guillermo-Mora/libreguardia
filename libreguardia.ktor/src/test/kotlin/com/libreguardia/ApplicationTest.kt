package com.libreguardia

import com.libreguardia.config.configureDatabase
import com.libreguardia.user.Priority
import com.libreguardia.user.Task
import com.libreguardia.config.configureRouting
import com.libreguardia.model.AbsenceTbl
import com.libreguardia.model.AcademicYearTbl
import com.libreguardia.model.BuildingTbl
import com.libreguardia.model.CourseTbl
import com.libreguardia.model.GroupTbl
import com.libreguardia.model.PlaceTbl
import com.libreguardia.model.PlaceTypeTbl
import com.libreguardia.model.ProfessionalFamilyTbl
import com.libreguardia.model.ScheduleActivityTbl
import com.libreguardia.model.ScheduleTbl
import com.libreguardia.model.ScheduleTemplateSlotTbl
import com.libreguardia.model.ScheduleTemplateTbl
import com.libreguardia.model.ServiceTbl
import com.libreguardia.model.TimeRangeTbl
import com.libreguardia.model.UserRoleTbl
import com.libreguardia.model.UserTbl
import com.libreguardia.model.ZoneTbl
import com.libreguardia.user.testRoutes
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.test.*

class ApplicationTest {
    @Test
    fun exposedToDatabaseTablesDifferences() = testApplication {
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )

            var missingColStatements: List<String>? = null
            transaction {
                missingColStatements =
                    SchemaUtils.addMissingColumnsStatements(
                        AbsenceTbl,
                        AcademicYearTbl,
                        BuildingTbl,
                        CourseTbl,
                        GroupTbl,
                        PlaceTbl,
                        PlaceTypeTbl,
                        ProfessionalFamilyTbl,
                        ScheduleActivityTbl,
                        ScheduleTbl,
                        ScheduleTemplateTbl,
                        ScheduleTemplateSlotTbl,
                        ServiceTbl,
                        TimeRangeTbl,
                        UserTbl,
                        UserRoleTbl,
                        ZoneTbl,
                    )
            }
            missingColStatements?.forEach { println(it) } ?: println("[null]")

        }
    }

    @Test
    fun tasksCanBeFoundByPriority() = testApplication {
        application {
            val repository = FakeTaskRepository()
            testRoutes(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/tasks/byPriority/Medium")
        val results = response.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedTaskNames = listOf("gardening", "painting")
        val actualTaskNames = results.map(Task::name)
        assertContentEquals(expectedTaskNames, actualTaskNames)
    }

    @Test
    fun invalidPriorityProduces400() = testApplication {
        application {
            val repository = FakeTaskRepository()
            testRoutes(repository)
            configureRouting()
        }
        val response = client.get("/tasks/byPriority/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedPriorityProduces404() = testApplication {
        application {
            val repository = FakeTaskRepository()
            testRoutes(repository)
            configureRouting()
        }

        val response = client.get("/tasks/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newTasksCanBeAdded() = testApplication {
        application {
            val repository = FakeTaskRepository()
            testRoutes(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val task = Task("swimming", "Go to the beach", Priority.Low)
        val response1 = client.post("/tasks") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )

            setBody(task)
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response2.status)

        val taskNames = response2
            .body<List<Task>>()
            .map { it.name }

        assertContains(taskNames, "swimming")
    }
}