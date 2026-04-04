package com.libreguardia

import com.libreguardia.config.configureDatabase
import com.libreguardia.config.configureDefaultHeaders
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
import com.libreguardia.repository.UserRoleRepository
import com.libreguardia.routing.UsersAPI
import com.libreguardia.service.UserService
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.SizedIterable
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.testcontainers.DockerClientFactory
import org.testcontainers.postgresql.PostgreSQLContainer
import java.math.BigDecimal
import kotlin.test.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

class ApplicationTest {
    @Test
    fun exposedTablesMatchToDatabase() = testApplication {
        val dbConnection = setupTestDBAndFlyway()

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
                    ScheduleRepository(),
                    UserRoleRepository()
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
    fun hardDeleteUserWithFutureReferences() = testApplication {
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
                    ScheduleRepository(),
                    UserRoleRepository()
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
        var usersAPI: List<UserResponseDTO> = client.get(UsersAPI()).body()
        assertTrue { usersAPI.isNotEmpty() }
        usersAPI.forEach { println(it) }

        val userUUID =
            withTransaction {
                UserTable.select(UserTable.id)
                    .where {
                        UserTable.name eq "Juan"
                    }.limit(1).map { it[UserTable.id].value }.first()
            }

        val userEntity = withTransaction { UserEntity.findById(userUUID)!! }

        val scheduleActivityEntity = withTransaction {
            ScheduleActivityEntity.new {
                name = "test"
                generatesService = true
                isEnabled = true
            }
        }

        val placeTypeEntity = withTransaction {
            PlaceTypeEntity.new {
                name = "test"
                isEnabled = true
            }
        }

        val zoneEntity = withTransaction {
            ZoneEntity.new {
                name = "test"
                isEnabled = true
            }
        }

        val placeEntity = withTransaction {
            PlaceEntity.new {
                name = "test"
                floor = null
                isEnabled = true
                building = null
                zone = zoneEntity
                placeType = placeTypeEntity
            }
        }

        //Create 2 absences, and its 2 services. One today ending 30 minutes before current time and the other
        // one day before current date

        //For the soft-delete test, create same 2 absences and 2 services but 1 day later and same day 30
        // minutes later
        val dateTimeNow: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val timeZone = TimeZone.currentSystemDefault()
        val dateTomorrw = dateTimeNow.toInstant(timeZone).plus(1.days).toLocalDateTime(timeZone).date
        val startTimeToday = dateTimeNow.toInstant(timeZone).plus(30.minutes).toLocalDateTime(timeZone).time
        val endTimeToday = dateTimeNow.toInstant(timeZone).plus(60.minutes).toLocalDateTime(timeZone).time
        withTransaction {
            AbsenceEntity.new {
                date = dateTomorrw
                startTime = startTimeToday
                endTime = endTimeToday
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = userEntity
            }
            AbsenceEntity.new {
                date = dateTimeNow.date
                startTime = startTimeToday
                endTime = endTimeToday
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = userEntity
            }
            val absence1 = AbsenceEntity.new {
                date = dateTomorrw
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            val absence2 = AbsenceEntity.new {
                date = dateTomorrw
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            ServiceEntity.new {
                pointsObtained = BigDecimal.ONE
                absence = absence1
                coverUser = null
                assignedUser = userEntity
            }
            ServiceEntity.new {
                pointsObtained = BigDecimal.ONE
                absence = absence2
                coverUser = null
                assignedUser = userEntity
            }
            ScheduleEntity.new {
                weekDay = WeekDay.WEDNESDAY
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = userEntity
            }
        }
        var schedules = withTransaction { ScheduleEntity.all() }
        assertTrue { withTransaction { schedules.count().toInt() } == 1 }

        var absences: SizedIterable<AbsenceEntity> = withTransaction { AbsenceEntity.all() }
        assertTrue { withTransaction { absences.count().toInt() } == 4 }

        var services: SizedIterable<ServiceEntity> = withTransaction { ServiceEntity.all() }
        assertTrue { withTransaction { services.count().toInt() } == 2 }

        val deleteResponse = client.delete(UsersAPI.UUID.Delete(UsersAPI.UUID(uuid = userUUID)))

        assertEquals(
            expected = HttpStatusCode.OK,
            actual = deleteResponse.status
        )

        usersAPI = client.get(UsersAPI()).body()
        usersAPI.forEach { println(it) }
        assertTrue { usersAPI.isEmpty() }

        absences = withTransaction { AbsenceEntity.all() }
        assertTrue { withTransaction { absences.count().toInt() } == 2 }

        services = withTransaction { ServiceEntity.all() }
        assertTrue { withTransaction { services.count().toInt() } == 2 }

        withTransaction { services.forEach { assertTrue { it.assignedUser == null } } }

        schedules = withTransaction { ScheduleEntity.all() }
        assertTrue { withTransaction { schedules.count().toInt() } == 0 }
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