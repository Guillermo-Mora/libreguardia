package com.libreguardia

import com.libreguardia.config.configureDatabase
import com.libreguardia.config.configureDefaultHeaders
import com.libreguardia.config.configureFlyway
import com.libreguardia.config.configureRouting
import com.libreguardia.config.configureSerialization
import com.libreguardia.config.configureStatusPages
import com.libreguardia.config.withTransaction
import com.libreguardia.db.*
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.dto.UserResponseDTO
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.routing.UsersAPI
import com.libreguardia.service.UserService
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
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
        val dbConnection = setupTestDBAndFlyway()
        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureSerialization()
            configureDefaultHeaders()
            configureRouting(
                UserService(
                    UserRepository(),
                    AbsenceRepository(),
                    ServiceRepository(),
                    ScheduleRepository()
                )
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        withTransaction {
            UserRoleEntity.new {
                name = "ADMIN"
            }
        }
        val roleUUID =
            withTransaction {
                UserRoleTable.select(UserRoleTable.id)
                    .where {
                        UserRoleTable.name eq "ADMIN"
                    }.limit(1).map { it[UserRoleTable.id].value }.first()
            }
        println(roleUUID)

        val createResponse = client.post(UsersAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                UserCreateDTO(
                    name = "Juan",
                    surname = "Martínez Hernández",
                    email = "juanmaher@edu.gva.es",
                    phoneNumber = "000000000",
                    password = "12345678",
                    isEnabled = true,
                    userRoleUUID = roleUUID
                )
            )
        }

        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )
        val usersAPI: List<UserResponseDTO> = client.get(UsersAPI()).body()
        assertTrue { usersAPI.isNotEmpty() }
    }

    @Test
    fun deleteUserWithoutReferences() = testApplication {
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
            //configureRouting(UserService(UserRepository()))

            //Here temporarly, as right now there's no UserRoleRepository
            withTransaction {
                UserRoleEntity.new {
                    name = "ADMIN"
                }
            }
        }
    }

    private fun setupTestDBAndFlyway(): DbConnection {
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

    private data class DbConnection(
        val url: String,
        val user: String,
        val password: String
    )
}