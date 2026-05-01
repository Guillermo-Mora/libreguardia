package com.libreguardia.integration
/*
import com.libreguardia.Testing
import com.libreguardia.config.configureDefaultHeaders
import com.libreguardia.config.configureSecurity
import com.libreguardia.db.configureDatabase
import com.libreguardia.dto.ScheduleActivityCreateDTO
import com.libreguardia.dto.ScheduleActivityResponseDTO
import com.libreguardia.exception.configureStatusPages
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.repository.ScheduleActivityRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.SessionRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.routing.configureRouting
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.AuthService
import com.libreguardia.service.ScheduleActivityService
import com.libreguardia.service.UserService
import com.libreguardia.validation.configureRequestValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.resources.Resources
import io.ktor.server.testing.TestApplication
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import at.favre.lib.crypto.bcrypt.BCrypt
import kotlin.time.Clock

class ScheduleActivityIntegrationTest {

    private fun setupTestApp(): TestApplication {
        return testApplication {
            val dbConnection = Testing.setupTestDBAndFlyway()
            application {
                configureDatabase(
                    url = dbConnection.url,
                    user = dbConnection.user,
                    password = dbConnection.password
                )
                configureStatusPages()
                configureRequestValidation()
                configureDefaultHeaders()
                val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()
                val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
                val clock = Clock.System

                val userRepository = UserRepository()
                val absenceRepository = AbsenceRepository()
                val serviceRepository = ServiceRepository()
                val scheduleRepository = ScheduleRepository()
                val scheduleActivityRepository = ScheduleActivityRepository()
                val sessionRepository = SessionRepository()
                val academicYearRepository = AcademicYearRepository()

                val userService = UserService(
                    bcryptVerifyer = bcryptVerifyer,
                    bcryptHasher = bcryptHasher,
                    clock = clock,
                    userRepository = userRepository,
                    absenceRepository = absenceRepository,
                    serviceRepository = serviceRepository,
                    scheduleRepository = scheduleRepository,
                    sessionRepository = sessionRepository
                )
                val authService = AuthService(
                    bcryptVerifyer = bcryptVerifyer,
                    bcryptHasher = bcryptHasher,
                    clock = clock,
                    userRepository = userRepository,
                    sessionRepository = sessionRepository,
                )
                val academicYearService = AcademicYearService(
                    repository = academicYearRepository
                )
                val scheduleActivityService = ScheduleActivityService(
                    repository = scheduleActivityRepository
                )

                configureSecurity(
                    authService = authService
                )
                com.libreguardia.config.configureCompression()
                com.libreguardia.config.configureMonitoring()
                com.libreguardia.config.configureSerialization()
                configureRouting(
                    authService = authService,
                    academicYearService = academicYearService,
                    scheduleActivityService = scheduleActivityService,
                    userService = userService
                )
            }
        }
    }

    @Test
    fun createAndListScheduleActivities() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureDefaultHeaders()
            val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()
            val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
            val clock = Clock.System

            val userRepository = UserRepository()
            val absenceRepository = AbsenceRepository()
            val serviceRepository = ServiceRepository()
            val scheduleRepository = ScheduleRepository()
            val scheduleActivityRepository = ScheduleActivityRepository()
            val sessionRepository = SessionRepository()
            val academicYearRepository = AcademicYearRepository()

            val userService = UserService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                absenceRepository = absenceRepository,
                serviceRepository = serviceRepository,
                scheduleRepository = scheduleRepository,
                sessionRepository = sessionRepository
            )
            val authService = AuthService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                sessionRepository = sessionRepository,
            )
            val academicYearService = AcademicYearService(
                repository = academicYearRepository
            )
            val scheduleActivityService = ScheduleActivityService(
                repository = scheduleActivityRepository
            )

            configureSecurity(
                authService = authService
            )
            com.libreguardia.config.configureCompression()
            com.libreguardia.config.configureMonitoring()
            com.libreguardia.config.configureSerialization()
            configureRouting(
                authService = authService,
                academicYearService = academicYearService,
                scheduleActivityService = scheduleActivityService,
                userService = userService
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val createResponse = client.post("/api/schedule-activity") {
            contentType(ContentType.Application.Json)
            setBody(
                ScheduleActivityCreateDTO(
                    name = "Docencia",
                    generatesService = false
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )

        val listResponse = client.get("/api/schedule-activity")
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = listResponse.status
        )

        val activities: List<ScheduleActivityResponseDTO> = listResponse.body()
        assertTrue { activities.isNotEmpty() }
        assertEquals(1, activities.size)
        assertEquals("Docencia", activities.first().name)
        assertEquals(false, activities.first().generatesService)
        assertEquals(true, activities.first().isEnabled)
    }

    @Test
    fun getScheduleActivityById() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureDefaultHeaders()
            val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()
            val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
            val clock = Clock.System

            val userRepository = UserRepository()
            val absenceRepository = AbsenceRepository()
            val serviceRepository = ServiceRepository()
            val scheduleRepository = ScheduleRepository()
            val scheduleActivityRepository = ScheduleActivityRepository()
            val sessionRepository = SessionRepository()
            val academicYearRepository = AcademicYearRepository()

            val userService = UserService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                absenceRepository = absenceRepository,
                serviceRepository = serviceRepository,
                scheduleRepository = scheduleRepository,
                sessionRepository = sessionRepository
            )
            val authService = AuthService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                sessionRepository = sessionRepository,
            )
            val academicYearService = AcademicYearService(
                repository = academicYearRepository
            )
            val scheduleActivityService = ScheduleActivityService(
                repository = scheduleActivityRepository
            )

            configureSecurity(
                authService = authService
            )
            com.libreguardia.config.configureCompression()
            com.libreguardia.config.configureMonitoring()
            com.libreguardia.config.configureSerialization()
            configureRouting(
                authService = authService,
                academicYearService = academicYearService,
                scheduleActivityService = scheduleActivityService,
                userService = userService
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val createResponse = client.post("/api/schedule-activity") {
            contentType(ContentType.Application.Json)
            setBody(
                ScheduleActivityCreateDTO(
                    name = "Tutoría",
                    generatesService = true
                )
            )
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        val listResponse = client.get("/api/schedule-activity")
        val activities: List<ScheduleActivityResponseDTO> = listResponse.body()
        val activityId = activities.first().id

        val getResponse = client.get("/api/schedule-activity/$activityId")
        assertEquals(HttpStatusCode.OK, getResponse.status)

        val activity: ScheduleActivityResponseDTO = getResponse.body()
        assertEquals("Tutoría", activity.name)
        assertEquals(true, activity.generatesService)
    }

    @Test
    fun editScheduleActivity() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureDefaultHeaders()
            val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()
            val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
            val clock = Clock.System

            val userRepository = UserRepository()
            val absenceRepository = AbsenceRepository()
            val serviceRepository = ServiceRepository()
            val scheduleRepository = ScheduleRepository()
            val scheduleActivityRepository = ScheduleActivityRepository()
            val sessionRepository = SessionRepository()
            val academicYearRepository = AcademicYearRepository()

            val userService = UserService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                absenceRepository = absenceRepository,
                serviceRepository = serviceRepository,
                scheduleRepository = scheduleRepository,
                sessionRepository = sessionRepository
            )
            val authService = AuthService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                sessionRepository = sessionRepository,
            )
            val academicYearService = AcademicYearService(
                repository = academicYearRepository
            )
            val scheduleActivityService = ScheduleActivityService(
                repository = scheduleActivityRepository
            )

            configureSecurity(
                authService = authService
            )
            com.libreguardia.config.configureCompression()
            com.libreguardia.config.configureMonitoring()
            com.libreguardia.config.configureSerialization()
            configureRouting(
                authService = authService,
                academicYearService = academicYearService,
                scheduleActivityService = scheduleActivityService,
                userService = userService
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val createResponse = client.post("/api/schedule-activity") {
            contentType(ContentType.Application.Json)
            setBody(
                ScheduleActivityCreateDTO(
                    name = "Guardia",
                    generatesService = false
                )
            )
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        val listResponse = client.get("/api/schedule-activity")
        val activities: List<ScheduleActivityResponseDTO> = listResponse.body()
        val activityId = activities.first().id

        val editResponse = client.patch("/api/schedule-activity/$activityId") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to "Guardia de Patio", "generatesService" to true))
        }
        assertEquals(HttpStatusCode.OK, editResponse.status)

        val getResponse = client.get("/api/schedule-activity/$activityId")
        val editedActivity: ScheduleActivityResponseDTO = getResponse.body()
        assertEquals("Guardia de Patio", editedActivity.name)
        assertEquals(true, editedActivity.generatesService)
    }

    @Test
    fun softDeleteScheduleActivity() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureDefaultHeaders()
            val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()
            val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
            val clock = Clock.System

            val userRepository = UserRepository()
            val absenceRepository = AbsenceRepository()
            val serviceRepository = ServiceRepository()
            val scheduleRepository = ScheduleRepository()
            val scheduleActivityRepository = ScheduleActivityRepository()
            val sessionRepository = SessionRepository()
            val academicYearRepository = AcademicYearRepository()

            val userService = UserService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                absenceRepository = absenceRepository,
                serviceRepository = serviceRepository,
                scheduleRepository = scheduleRepository,
                sessionRepository = sessionRepository
            )
            val authService = AuthService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                sessionRepository = sessionRepository,
            )
            val academicYearService = AcademicYearService(
                repository = academicYearRepository
            )
            val scheduleActivityService = ScheduleActivityService(
                repository = scheduleActivityRepository
            )

            configureSecurity(
                authService = authService
            )
            com.libreguardia.config.configureCompression()
            com.libreguardia.config.configureMonitoring()
            com.libreguardia.config.configureSerialization()
            configureRouting(
                authService = authService,
                academicYearService = academicYearService,
                scheduleActivityService = scheduleActivityService,
                userService = userService
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val createResponse = client.post("/api/schedule-activity") {
            contentType(ContentType.Application.Json)
            setBody(
                ScheduleActivityCreateDTO(
                    name = "Recreo",
                    generatesService = false
                )
            )
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        var listResponse = client.get("/api/schedule-activity")
        var activities: List<ScheduleActivityResponseDTO> = listResponse.body()
        assertEquals(1, activities.size)
        val activityId = activities.first().id

        val deleteResponse = client.delete("/api/schedule-activity/$activityId")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        listResponse = client.get("/api/schedule-activity")
        activities = listResponse.body()
        assertTrue { activities.isEmpty() }
    }

    @Test
    fun toggleEnabledScheduleActivity() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureDefaultHeaders()
            val bcryptVerifyer: BCrypt.Verifyer = BCrypt.verifyer()
            val bcryptHasher: BCrypt.Hasher = BCrypt.withDefaults()
            val clock = Clock.System

            val userRepository = UserRepository()
            val absenceRepository = AbsenceRepository()
            val serviceRepository = ServiceRepository()
            val scheduleRepository = ScheduleRepository()
            val scheduleActivityRepository = ScheduleActivityRepository()
            val sessionRepository = SessionRepository()
            val academicYearRepository = AcademicYearRepository()

            val userService = UserService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                absenceRepository = absenceRepository,
                serviceRepository = serviceRepository,
                scheduleRepository = scheduleRepository,
                sessionRepository = sessionRepository
            )
            val authService = AuthService(
                bcryptVerifyer = bcryptVerifyer,
                bcryptHasher = bcryptHasher,
                clock = clock,
                userRepository = userRepository,
                sessionRepository = sessionRepository,
            )
            val academicYearService = AcademicYearService(
                repository = academicYearRepository
            )
            val scheduleActivityService = ScheduleActivityService(
                repository = scheduleActivityRepository
            )

            configureSecurity(
                authService = authService
            )
            com.libreguardia.config.configureCompression()
            com.libreguardia.config.configureMonitoring()
            com.libreguardia.config.configureSerialization()
            configureRouting(
                authService = authService,
                academicYearService = academicYearService,
                scheduleActivityService = scheduleActivityService,
                userService = userService
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val createResponse = client.post("/api/schedule-activity") {
            contentType(ContentType.Application.Json)
            setBody(
                ScheduleActivityCreateDTO(
                    name = "Comedor",
                    generatesService = true
                )
            )
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        var listResponse = client.get("/api/schedule-activity")
        var activities: List<ScheduleActivityResponseDTO> = listResponse.body()
        val activityId = activities.first().id
        assertTrue { activities.first().isEnabled }

        val toggleResponse = client.patch("/api/schedule-activity/$activityId/toggle-enabled") {
            contentType(ContentType.Application.Json)
            setBody(false)
        }
        assertEquals(HttpStatusCode.OK, toggleResponse.status)

        listResponse = client.get("/api/schedule-activity")
        activities = listResponse.body()
        assertTrue { activities.isEmpty() }

        val toggleBackResponse = client.patch("/api/schedule-activity/$activityId/toggle-enabled") {
            contentType(ContentType.Application.Json)
            setBody(true)
        }
        assertEquals(HttpStatusCode.OK, toggleBackResponse.status)

        listResponse = client.get("/api/schedule-activity")
        activities = listResponse.body()
        assertEquals(1, activities.size)
        assertTrue { activities.first().isEnabled }
    }
}

*/