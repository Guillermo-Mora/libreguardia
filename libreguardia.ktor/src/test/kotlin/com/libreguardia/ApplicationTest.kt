package com.libreguardia

import com.libreguardia.config.configureDatabase
import com.libreguardia.config.configureDefaultHeaders
import com.libreguardia.config.configureFlyway
import com.libreguardia.config.configureRouting
import com.libreguardia.config.configureSerialization
import com.libreguardia.config.withTransaction
import com.libreguardia.db.*
import com.libreguardia.dto.UserRequestDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.repository.UserRepository
import com.libreguardia.routing.Users
import com.libreguardia.routing.userRouting
import com.libreguardia.service.UserService
import com.libreguardia.user.Priority
import com.libreguardia.user.Task
import com.libreguardia.user.testRoutes
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.*
import io.ktor.server.resources.Resources
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.DockerClientFactory
import org.testcontainers.postgresql.PostgreSQLContainer
import kotlin.test.*

class ApplicationTest {
    @Test
    fun exposedTablesMatchToDatabase() = testApplication {
        environment {
            config = ApplicationConfig("resources/application.yaml")
        }
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )
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
    }

    @Test
    fun dockerIsAccessible() = assertTrue(DockerClientFactory.instance().isDockerAvailable)

    @Test
    fun addUserAndList() = testApplication {
        application {
            val testDB = PostgreSQLContainer("postgres:18.3").apply {
                withDatabaseName("testdb")
                withUsername("test")
                withPassword("test")
                start()
            }
            val dbUrl = testDB.jdbcUrl
            val dbUser = testDB.username
            val dbPassword = testDB.password
            configureDatabase(
                url = dbUrl,
                user = dbUser,
                password = dbPassword
            )
            configureFlyway(
                url = dbUrl,
                user = dbUser,
                password = dbPassword
            )
            configureSerialization()
            configureDefaultHeaders()
            configureRouting(UserService(UserRepository()))

            //Here temporarly, as right now there's no UserRoleRepository
            withTransaction {
                UserRoleEntity.new {
                    name = "ADMIN"
                }
            }
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(io.ktor.client.plugins.resources.Resources)
        }

        val createResponse = client.post(Users()) {
            contentType(ContentType.Application.Json)
            setBody(
                UserRequestDTO(
                    name = "Juan",
                    surname = "Martínez Hernández",
                    email = "juanmaher@edu.gva.es",
                    phoneNumber = "000000000",
                    password = "123",
                    isEnabled = true,
                    userRole = "ADMIN"
                )
            )
        }

        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )
        val users: List<UserResponseDTO> = client.get(Users()).body()
        assertTrue { users.isNotEmpty() }
    }


    @Test
    fun tasksCanBeFoundByPriority() = testApplication {
        application {
            val repository = FakeTaskRepository()
            testRoutes(repository)
            //configureRouting()
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
            //configureRouting()
        }
        val response = client.get("/tasks/byPriority/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedPriorityProduces404() = testApplication {
        application {
            val repository = FakeTaskRepository()
            testRoutes(repository)
            //configureRouting()
        }

        val response = client.get("/tasks/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newTasksCanBeAdded() = testApplication {
        application {
            val repository = FakeTaskRepository()
            testRoutes(repository)
            //configureRouting()
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