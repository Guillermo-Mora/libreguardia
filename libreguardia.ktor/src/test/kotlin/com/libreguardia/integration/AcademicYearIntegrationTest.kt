package com.libreguardia.integration

/*

import com.libreguardia.Testing
import com.libreguardia.config.configureDefaultHeaders
import com.libreguardia.config.configureRouting
import com.libreguardia.config.configureSerialization
import com.libreguardia.db.configureDatabase
import com.libreguardia.db.configureFlyway
import com.libreguardia.db.model.AcademicYearEntity
import com.libreguardia.db.model.AcademicYearTable
import com.libreguardia.dto.AcademicYearCreateDTO
import com.libreguardia.dto.AcademicYearEditDTO
import com.libreguardia.dto.AcademicYearResponseDTO
import com.libreguardia.exception.configureStatusPages
import com.libreguardia.exception.validation.configureRequestValidation
import com.libreguardia.routing.modules.AcademicYearAPI
import com.libreguardia.routing.modules.AcademicYearByUUID
import com.libreguardia.routing.modules.academicYearRouting
import com.libreguardia.service.AcademicYearService
import com.libreguardia.repository.AcademicYearRepository
import com.libreguardia.repository.AbsenceRepository
import com.libreguardia.repository.ServiceRepository
import com.libreguardia.repository.ScheduleRepository
import com.libreguardia.repository.UserRepository
import com.libreguardia.repository.UserRoleRepository
import com.libreguardia.service.AcademicYearService
import com.libreguardia.service.AuthService
import com.libreguardia.service.UserService
import com.libreguardia.service.JwtService
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.createTestApplication
import io.ktor.server.testing.withTestApplication
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import org.jetbrains.exposed.v1.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AcademicYearIntegrationTest {
    
    @Test
    fun testCreateAndListAcademicYear() = withTestApplication {
        val testEngine = createTestApplication()
        val dbConnection = Testing.setupTestDBAndFlyway()
        
        testEngine.application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureFlyway(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureSerialization()
            configureDefaultHeaders()
            configureRouting(
                authService = createAuthService(),
                academicYearService = AcademicYearService(AcademicYearRepository()),
                userService = createUserService()
            )
        }
        
        val client = testEngine.createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }
        
        // Create AcademicYear
        val createResponse = client.post(AcademicYearAPI()) {
            contentType(ContentType.Application.Json)
            setBody(AcademicYearCreateDTO(
                name = "2024-2025",
                startDate = LocalDate(2024, 9, 1),
                endDate = LocalDate(2025, 6, 30)
            ))
        }
        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )
        
        // Verify created in database
        val createdYears = transaction {
            AcademicYearTable.select { AcademicYearTable.name eq "2024-2025" }.toList()
        }
        assertTrue { createdYears.isNotEmpty() }
        
        // List all AcademicYears
        val listResponse = client.get(AcademicYearAPI())
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = listResponse.status
        )
        val academicYears = listResponse.body<List<AcademicYearResponseDTO>>()
        assertTrue { academicYears.isNotEmpty() }
    }
    
    @Test
    fun testGetAcademicYearByUUID() = withTestApplication {
        val testEngine = createTestApplication()
        val dbConnection = Testing.setupTestDBAndFlyway()
        
        testEngine.application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureFlyway(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureSerialization()
            configureDefaultHeaders()
            configureRouting(
                authService = createAuthService(),
                academicYearService = AcademicYearService(AcademicYearRepository()),
                userService = createUserService()
            )
        }
        
        val client = testEngine.createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }
        
        // Create an AcademicYear first
        val testUuid = UUID.randomUUID()
        transaction {
            AcademicYearTable.insert {
                it[AcademicYearTable.id] = EntityID(testUuid, AcademicYearTable)
                it[AcademicYearTable.name] = "2024-2025"
                it[AcademicYearTable.startDate] = LocalDate(2024, 9, 1)
                it[AcademicYearTable.endDate] = LocalDate(2025, 6, 30)
                it[AcademicYearTable.isEnabled] = true
            }
        }
        
        // Get by UUID
        val getResponse = client.get(AcademicYearByUUID(UUID = testUuid))
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = getResponse.status
        )
        
        val academicYear = getResponse.body<AcademicYearResponseDTO>()
        assertEquals(
            expected = "2024-2025",
            actual = academicYear.name
        )
    }
    
    @Test
    fun testEditAcademicYear() = withTestApplication {
        val testEngine = createTestApplication()
        val dbConnection = Testing.setupTestDBAndFlyway()
        
        testEngine.application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureFlyway(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureSerialization()
            configureDefaultHeaders()
            configureRouting(
                authService = createAuthService(),
                academicYearService = AcademicYearService(AcademicYearRepository()),
                userService = createUserService()
            )
        }
        
        val client = testEngine.createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }
        
        // Create an AcademicYear first
        val testUuid = UUID.randomUUID()
        transaction {
            AcademicYearTable.insert {
                it[AcademicYearTable.id] = EntityID(testUuid, AcademicYearTable)
                it[AcademicYearTable.name] = "2024-2025"
                it[AcademicYearTable.startDate] = LocalDate(2024, 9, 1)
                it[AcademicYearTable.endDate] = LocalDate(2025, 6, 30)
                it[AcademicYearTable.isEnabled] = true
            }
        }
        
        // Edit AcademicYear
        val editResponse = client.patch(AcademicYearByUUID(UUID = testUuid)) {
            contentType(ContentType.Application.Json)
            setBody(AcademicYearEditDTO(name = "2025-2026"))
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = editResponse.status
        )
        
        // Verify edit in database
        val updated = transaction {
            AcademicYearTable.select { AcademicYearTable.id eq testUuid }
                .map { it[AcademicYearTable.name] }
                .first()
        }
        assertEquals(
            expected = "2025-2026",
            actual = updated
        )
    }
    
    @Test
    fun testDeleteAcademicYear() = withTestApplication {
        val testEngine = createTestApplication()
        val dbConnection = Testing.setupTestDBAndFlyway()
        
        testEngine.application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureFlyway(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            configureRequestValidation()
            configureSerialization()
            configureDefaultHeaders()
            configureRouting(
                authService = createAuthService(),
                academicYearService = AcademicYearService(AcademicYearRepository()),
                userService = createUserService()
            )
        }
        
        val client = testEngine.createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }
        
        // Create an AcademicYear first
        val testUuid = UUID.randomUUID()
        transaction {
            AcademicYearTable.insert {
                it[AcademicYearTable.id] = EntityID(testUuid, AcademicYearTable)
                it[AcademicYearTable.name] = "2024-2025"
                it[AcademicYearTable.startDate] = LocalDate(2024, 9, 1)
                it[AcademicYearTable.endDate] = LocalDate(2025, 6, 30)
                it[AcademicYearTable.isEnabled] = true
            }
        }
        
        // Delete AcademicYear (soft delete)
        val deleteResponse = client.delete(AcademicYearByUUID(UUID = testUuid))
        assertEquals(
            expected = HttpStatusCode.NoContent,
            actual = deleteResponse.status
        )
        
        // Verify isEnabled = false in database
        val isEnabled = transaction {
            AcademicYearTable.select { AcademicYearTable.id eq testUuid }
                .map { it[AcademicYearTable.isEnabled] }
                .first()
        }
        assertEquals(
            expected = false,
            actual = isEnabled
        )
    }
    
    private fun createAuthService(): AuthService {
        return AuthService(
            bcryptVerifyer = at.favre.lib.crypto.bcrypt.BCrypt.verifyer(),
            bcryptHasher = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults(),
            clock = kotlin.time.Clock.System,
            userRepository = UserRepository(),
            jwtService = JwtService(userRepository = UserRepository()),
            refreshTokenRepository = com.libreguardia.repository.RefreshTokenRepository()
        )
    }
    
    private fun createUserService(): UserService {
        return UserService(
            bcryptVerifyer = at.favre.lib.crypto.bcrypt.BCrypt.verifyer(),
            bcryptHasher = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults(),
            clock = kotlin.time.Clock.System,
            userRepository = UserRepository(),
            absenceRepository = AbsenceRepository(),
            serviceRepository = ServiceRepository(),
            scheduleRepository = ScheduleRepository(),
            userRoleRepository = UserRoleRepository(),
            refreshTokenRepository = com.libreguardia.repository.RefreshTokenRepository()
        )
    }
}

 */