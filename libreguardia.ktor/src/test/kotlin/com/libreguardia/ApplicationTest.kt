package com.libreguardia

import com.libreguardia.config.configureDatabase
import com.libreguardia.config.configureRouting
import com.libreguardia.db.*
import com.libreguardia.dto.ProfessionalFamilyRequestDTO
import com.libreguardia.dto.ProfessionalFamilyResponseDTO
import com.libreguardia.dto.AcademicYearRequestDTO
import com.libreguardia.dto.AcademicYearResponseDTO
import com.libreguardia.dto.UserRequestDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.routing.Users
import com.libreguardia.repository.ProfessionalFamilyRepository
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.service.ProfessionalFamilyService
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.UserService
import com.libreguardia.user.Priority
import com.libreguardia.user.Task
import com.libreguardia.user.testRoutes
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.content
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    fun addUserAndList() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        application {

        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }

        }

        val createResponse = client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Juan","surname":"Martínez Hernández","email":"juanmaher@edu.gva.es","phoneNumber":"000000000","password":"123","isEnabled":true,"userRole":"ADMIN"}""")
        }
        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )
        val users: List<UserResponseDTO> = client.get("/api/users").body()
        assertTrue {users.isNotEmpty()}
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

    @Test
    fun professionalFamilyCrud() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )
            val repository = ProfessionalFamilyRepository()
            val service = ProfessionalFamilyService(repository)
            configureRouting(
                academicYearService = AcademicYearService(AcademicYearRepository()),
                professionalFamilyService = service,
                userService = UserService(UserRepository())
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val initialFamilies: List<ProfessionalFamilyResponseDTO> = client.get("/api/professional-families").body()
        val initialCount = initialFamilies.size

        val createResponse = client.post("/api/professional-families") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Informática", "isEnabled": true}""")
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        val createdFamily: ProfessionalFamilyResponseDTO = createResponse.body()
        assertEquals("Informática", createdFamily.name)
        assertTrue(createdFamily.isEnabled)

        val getByIdResponse = client.get("/api/professional-families/${createdFamily.id}")
        assertEquals(HttpStatusCode.OK, getByIdResponse.status)
        val retrievedFamily: ProfessionalFamilyResponseDTO = getByIdResponse.body()
        assertEquals(createdFamily.id, retrievedFamily.id)

        val updateResponse = client.put("/api/professional-families/${createdFamily.id}") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Informática y Comunicaciones", "isEnabled": false}""")
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        val updatedFamily: ProfessionalFamilyResponseDTO = updateResponse.body()
        assertEquals("Informática y Comunicaciones", updatedFamily.name)
        assertFalse(updatedFamily.isEnabled)

        val deleteResponse = client.delete("/api/professional-families/${createdFamily.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val notFoundResponse = client.get("/api/professional-families/${createdFamily.id}")
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)

        val finalFamilies: List<ProfessionalFamilyResponseDTO> = client.get("/api/professional-families").body()
        assertEquals(initialCount, finalFamilies.size)
    }

    @Test
    fun professionalFamilyValidation() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )
            val repository = ProfessionalFamilyRepository()
            val service = ProfessionalFamilyService(repository)
            configureRouting(
                academicYearService = AcademicYearService(AcademicYearRepository()),
                professionalFamilyService = service,
                userService = UserService(UserRepository())
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val invalidResponse = client.post("/api/professional-families") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "", "isEnabled": true}""")
        }
        assertEquals(HttpStatusCode.BadRequest, invalidResponse.status)
    }

    @Test
    fun professionalFamilyNotFound() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )
            val repository = ProfessionalFamilyRepository()
            val service = ProfessionalFamilyService(repository)
            configureRouting(
                academicYearService = AcademicYearService(AcademicYearRepository()),
                professionalFamilyService = service,
                userService = UserService(UserRepository())
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val notFoundResponse = client.get("/api/professional-families/00000000-0000-0000-0000-000000000000")
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)
    }

    @Test
    fun academicYearCrud() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )
            val repository = AcademicYearRepository()
            val service = AcademicYearService(repository)
            configureRouting(
                academicYearService = service,
                professionalFamilyService = ProfessionalFamilyService(ProfessionalFamilyRepository()),
                userService = UserService(UserRepository())
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val initialYears: List<AcademicYearResponseDTO> = client.get("/api/academic-years").body()
        val initialCount = initialYears.size

        val createResponse = client.post("/api/academic-years") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"2024-2025","startDate":"2024-09-01","endDate":"2025-06-30"}""")
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        val createdYear: AcademicYearResponseDTO = createResponse.body()
        assertEquals("2024-2025", createdYear.name)

        val getByIdResponse = client.get("/api/academic-years/${createdYear.id}")
        assertEquals(HttpStatusCode.OK, getByIdResponse.status)
        val retrievedYear: AcademicYearResponseDTO = getByIdResponse.body()
        assertEquals(createdYear.id, retrievedYear.id)

        val updateResponse = client.put("/api/academic-years/${createdYear.id}") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"2025-2026","startDate":"2025-09-01","endDate":"2026-06-30"}""")
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        val updatedYear: AcademicYearResponseDTO = updateResponse.body()
        assertEquals("2025-2026", updatedYear.name)

        val deleteResponse = client.delete("/api/academic-years/${createdYear.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val notFoundResponse = client.get("/api/academic-years/${createdYear.id}")
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)

        val finalYears: List<AcademicYearResponseDTO> = client.get("/api/academic-years").body()
        assertEquals(initialCount, finalYears.size)
    }

    @Test
    fun academicYearValidation() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )
            val repository = AcademicYearRepository()
            val service = AcademicYearService(repository)
            configureRouting(
                academicYearService = service,
                professionalFamilyService = ProfessionalFamilyService(ProfessionalFamilyRepository()),
                userService = UserService(UserRepository())
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val invalidResponse = client.post("/api/academic-years") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"","startDate":"2024-09-01","endDate":"2024-06-30"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, invalidResponse.status)
    }

    @Test
    fun academicYearNotFound() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        application {
            configureDatabase(
                url = "jdbc:postgresql://localhost:5432/libreguardia",
                user = "postgres",
                password = "libreguardia"
            )
            val repository = AcademicYearRepository()
            val service = AcademicYearService(repository)
            configureRouting(
                academicYearService = service,
                professionalFamilyService = ProfessionalFamilyService(ProfessionalFamilyRepository()),
                userService = UserService(UserRepository())
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val notFoundResponse = client.get("/api/academic-years/00000000-0000-0000-0000-000000000000")
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)
    }
}