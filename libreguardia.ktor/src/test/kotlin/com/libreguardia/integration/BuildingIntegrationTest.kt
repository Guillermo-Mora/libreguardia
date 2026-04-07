package com.libreguardia.integration

import com.libreguardia.Testing
import com.libreguardia.config.*
import com.libreguardia.db.*
import com.libreguardia.dto.BuildingRequestDTO
import com.libreguardia.dto.BuildingResponseDTO
import com.libreguardia.repository.*
import com.libreguardia.routing.BuildingsAPI
import com.libreguardia.service.BuildingService
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BuildingIntegrationTest {
    @Test
    fun createBuildingAndList() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
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
                buildingService = BuildingService(BuildingRepository()),
                userService = com.libreguardia.service.UserService(
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

        val initialBuildings: List<BuildingResponseDTO> = client.get(BuildingsAPI()).body()
        val initialCount = initialBuildings.size

        val createResponse = client.post(BuildingsAPI()) {
            contentType(ContentType.Application.Json)
            setBody(BuildingRequestDTO(name = "Edificio Principal", isEnabled = true))
        }
        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )

        val finalBuildings: List<BuildingResponseDTO> = client.get(BuildingsAPI()).body()
        assertEquals(initialCount + 1, finalBuildings.size)

        val buildingUUID = withTransaction {
            BuildingTable.select(BuildingTable.id)
                .where {
                    BuildingTable.name eq "Edificio Principal"
                }.limit(1).map { it[BuildingTable.id].value }.first()
        }

        val getBuilding = client.get(BuildingsAPI.BuildingUUID(uuid = buildingUUID))
        val buildingDTO = getBuilding.body<BuildingResponseDTO>()
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = getBuilding.status
        )
        assertTrue { buildingDTO.name == "Edificio Principal" }
    }

    @Test
    fun updateAndDeleteBuilding() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
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
                buildingService = BuildingService(BuildingRepository()),
                userService = com.libreguardia.service.UserService(
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

        val createResponse = client.post(BuildingsAPI()) {
            contentType(ContentType.Application.Json)
            setBody(BuildingRequestDTO(name = "Edificio A", isEnabled = true))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        val buildingUUID = withTransaction {
            BuildingTable.select(BuildingTable.id)
                .where {
                    BuildingTable.name eq "Edificio A"
                }.limit(1).map { it[BuildingTable.id].value }.first()
        }

        val putResponse = client.put(BuildingsAPI.BuildingUUID(uuid = buildingUUID)) {
            contentType(ContentType.Application.Json)
            setBody(BuildingRequestDTO(name = "Edificio B", isEnabled = false))
        }
        assertEquals(HttpStatusCode.OK, putResponse.status)

        val getUpdated = client.get(BuildingsAPI.BuildingUUID(uuid = buildingUUID))
        val updatedDTO = getUpdated.body<BuildingResponseDTO>()
        assertEquals("Edificio B", updatedDTO.name)
        assertEquals(false, updatedDTO.isEnabled)

        val deleteResponse = client.delete(BuildingsAPI.BuildingUUID(uuid = buildingUUID))
        assertEquals(HttpStatusCode.OK, deleteResponse.status)
    }
}