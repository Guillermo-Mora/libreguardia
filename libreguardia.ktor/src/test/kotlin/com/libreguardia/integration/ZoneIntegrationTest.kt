package com.libreguardia.integration
/*

import com.libreguardia.Testing
import com.libreguardia.db.configureDatabase
import com.libreguardia.db.model.ZoneEntity
import com.libreguardia.db.model.ZoneTable
import com.libreguardia.dto.ZoneCreateDTO
import com.libreguardia.dto.ZoneEditDTO
import com.libreguardia.dto.ZoneResponseDTO
import com.libreguardia.exception.configureStatusPages
import com.libreguardia.repository.ZoneRepository
import com.libreguardia.routing.module.ZoneAPI
import com.libreguardia.routing.module.ZoneByUUID
import com.libreguardia.service.ZoneService
import com.libreguardia.util.withTransaction
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.patch
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZoneIntegrationTest {

    private fun ApplicationTestBuilder.setupApplication(dbConnection: com.libreguardia.DbConnection) {
        application {
            configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            configureStatusPages()
            install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
            install(Resources)
            routing {
                zoneRouting(ZoneService(ZoneRepository()))
            }
        }
    }

    private fun ApplicationTestBuilder.createClient() = createClient {
        install(ContentNegotiation) { json() }
        install(io.ktor.client.plugins.resources.Resources)
    }

    @Test
    fun `list all zones returns empty list when no zones exist`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        val response = client.get(ZoneAPI())
        assertEquals(HttpStatusCode.OK, response.status)
        val zones: List<ZoneResponseDTO> = response.body()
        assertTrue(zones.isEmpty())
    }

    @Test
    fun `create zone returns 201 and zone appears in list`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        val createResponse = client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "Zona A"))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        val zones: List<ZoneResponseDTO> = client.get(ZoneAPI()).body()
        assertTrue(zones.isNotEmpty())
        assertTrue(zones.any { it.name == "Zona A" })
    }

    @Test
    fun `create zone with blank name returns 400`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        val response = client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "   "))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `create zone with duplicate name returns 409`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "Zona Duplicada"))
        }

        val response = client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "Zona Duplicada"))
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun `edit zone updates name correctly`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "Zona Original"))
        }

        val zoneUUID = withTransaction {
            ZoneTable.select(ZoneTable.id)
                .where { ZoneTable.name eq "Zona Original" }
                .limit(1)
                .map { it[ZoneTable.id].value }
                .first()
        }

        val editResponse = client.patch(ZoneByUUID(uuid = zoneUUID)) {
            contentType(ContentType.Application.Json)
            setBody(ZoneEditDTO(name = "Zona Editada"))
        }
        assertEquals(HttpStatusCode.OK, editResponse.status)

        val zoneEntity = withTransaction { ZoneEntity.findById(zoneUUID)!! }
        assertEquals("Zona Editada", zoneEntity.name)
    }

    @Test
    fun `edit zone with blank name returns 400`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "Zona Para Editar"))
        }

        val zoneUUID = withTransaction {
            ZoneTable.select(ZoneTable.id)
                .where { ZoneTable.name eq "Zona Para Editar" }
                .limit(1)
                .map { it[ZoneTable.id].value }
                .first()
        }

        val editResponse = client.patch(ZoneByUUID(uuid = zoneUUID)) {
            contentType(ContentType.Application.Json)
            setBody(ZoneEditDTO(name = ""))
        }
        assertEquals(HttpStatusCode.BadRequest, editResponse.status)
    }

    @Test
    fun `edit zone with non-existent uuid returns 404`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        val fakeUUID = java.util.UUID.randomUUID()
        val response = client.patch(ZoneByUUID(uuid = fakeUUID)) {
            contentType(ContentType.Application.Json)
            setBody(ZoneEditDTO(name = "No existe"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `soft delete zone sets isEnabled to false`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "Zona a Eliminar"))
        }

        val zoneUUID = withTransaction {
            ZoneTable.select(ZoneTable.id)
                .where { ZoneTable.name eq "Zona a Eliminar" }
                .limit(1)
                .map { it[ZoneTable.id].value }
                .first()
        }

        val deleteResponse = client.delete(ZoneByUUID(uuid = zoneUUID))
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val zoneEntity = withTransaction { ZoneEntity.findById(zoneUUID)!! }
        assertFalse(zoneEntity.isEnabled)
    }

    @Test
    fun `soft delete zone with non-existent uuid returns 404`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        val fakeUUID = java.util.UUID.randomUUID()
        val response = client.delete(ZoneByUUID(uuid = fakeUUID))
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `full crud flow creates edits and soft deletes zone`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = createClient()

        // Create
        val createResponse = client.post(ZoneAPI()) {
            contentType(ContentType.Application.Json)
            setBody(ZoneCreateDTO(name = "Zona CRUD"))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        // List
        val zones: List<ZoneResponseDTO> = client.get(ZoneAPI()).body()
        assertTrue(zones.any { it.name == "Zona CRUD" && it.isEnabled })

        val zoneUUID = withTransaction {
            ZoneTable.select(ZoneTable.id)
                .where { ZoneTable.name eq "Zona CRUD" }
                .limit(1)
                .map { it[ZoneTable.id].value }
                .first()
        }

        // Edit
        val editResponse = client.patch(ZoneByUUID(uuid = zoneUUID)) {
            contentType(ContentType.Application.Json)
            setBody(ZoneEditDTO(name = "Zona CRUD Editada"))
        }
        assertEquals(HttpStatusCode.OK, editResponse.status)

        val editedZone = withTransaction { ZoneEntity.findById(zoneUUID)!! }
        assertEquals("Zona CRUD Editada", editedZone.name)
        assertTrue(editedZone.isEnabled)

        // Soft delete
        val deleteResponse = client.delete(ZoneByUUID(uuid = zoneUUID))
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val deletedZone = withTransaction { ZoneEntity.findById(zoneUUID)!! }
        assertFalse(deletedZone.isEnabled)
    }
}

 */