package com.libreguardia.integration

/*
class PlaceTypeIntegrationTest {

    private fun ApplicationTestBuilder.setupApplication(dbConnection: com.libreguardia.DbConnection) {
        application {
            com.libreguardia.db.configureDatabase(
                url = dbConnection.url,
                user = dbConnection.user,
                password = dbConnection.password
            )
            com.libreguardia.exception.configureStatusPages()
            install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) { json() }
            install(io.ktor.server.resources.Resources)
            routing {
                com.libreguardia.routing.modules.placeTypeRouting(
                    com.libreguardia.service.PlaceTypeService(com.libreguardia.repository.PlaceTypeRepository())
                )
            }
        }
    }

    private fun ApplicationTestBuilder.buildClient() = createClient {
        install(ContentNegotiation) { json() }
        install(Resources)
    }

    @Test
    fun `list all place types returns empty list when no place types exist`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        val response = client.get(PlaceTypeAPI())
        assertEquals(HttpStatusCode.OK, response.status)
        val placeTypes: List<PlaceTypeResponseDTO> = response.body()
        assertTrue { placeTypes.isEmpty() }
    }

    @Test
    fun `create place type returns 201 and appears in list`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        val createResponse = client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Aula"))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        val placeTypes: List<PlaceTypeResponseDTO> = client.get(PlaceTypeAPI()).body()
        assertTrue { placeTypes.isNotEmpty() }
        assertTrue { placeTypes.any { it.name == "Aula" } }
    }

    @Test
    fun `create place type with blank name returns 400`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        val response = client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "   "))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `create place type with duplicate name returns 409`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Duplicado"))
        }
        val response = client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Duplicado"))
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun `get place type by uuid returns correct place type`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Laboratorio"))
        }
        val uuid = withTransaction {
            PlaceTypeEntity.find { PlaceTypeTable.name eq "Laboratorio" }.first().id.value
        }

        val response = client.get(PlaceTypeByUUID(uuid = uuid))
        assertEquals(HttpStatusCode.OK, response.status)
        val placeType: PlaceTypeResponseDTO = response.body()
        assertEquals("Laboratorio", placeType.name)
    }

    @Test
    fun `get place type by non-existent uuid returns 404`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        val response = client.get(PlaceTypeByUUID(uuid = java.util.UUID.randomUUID()))
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `edit place type updates name correctly`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Taller"))
        }
        val uuid = withTransaction {
            PlaceTypeEntity.find { PlaceTypeTable.name eq "Taller" }.first().id.value
        }

        val editResponse = client.patch(PlaceTypeByUUID(uuid = uuid)) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeEditDTO(name = "Taller Editado"))
        }
        assertEquals(HttpStatusCode.OK, editResponse.status)

        val name = withTransaction { PlaceTypeEntity.findById(uuid)!!.name }
        assertEquals("Taller Editado", name)
    }

    @Test
    fun `edit place type with blank name returns 400`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Gimnasio"))
        }
        val uuid = withTransaction {
            PlaceTypeEntity.find { PlaceTypeTable.name eq "Gimnasio" }.first().id.value
        }

        val response = client.patch(PlaceTypeByUUID(uuid = uuid)) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeEditDTO(name = ""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `edit place type with non-existent uuid returns 404`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        val response = client.patch(PlaceTypeByUUID(uuid = java.util.UUID.randomUUID())) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeEditDTO(name = "No existe"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `soft delete place type sets isEnabled to false`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Biblioteca"))
        }
        val uuid = withTransaction {
            PlaceTypeEntity.find { PlaceTypeTable.name eq "Biblioteca" }.first().id.value
        }

        val deleteResponse = client.delete(PlaceTypeByUUID(uuid = uuid))
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val isEnabled = withTransaction { PlaceTypeEntity.findById(uuid)!!.isEnabled }
        assertTrue { !isEnabled }
    }

    @Test
    fun `soft delete place type with non-existent uuid returns 404`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        val response = client.delete(PlaceTypeByUUID(uuid = java.util.UUID.randomUUID()))
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `full crud flow for place type`() = testApplication {
        val dbConnection = Testing.setupTestDBAndFlyway()
        setupApplication(dbConnection)
        val client = buildClient()

        val createResponse = client.post(PlaceTypeAPI()) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeCreateDTO(name = "Sala CRUD"))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        val placeTypes: List<PlaceTypeResponseDTO> = client.get(PlaceTypeAPI()).body()
        assertTrue { placeTypes.any { it.name == "Sala CRUD" && it.isEnabled } }

        val uuid = withTransaction {
            PlaceTypeEntity.find { PlaceTypeTable.name eq "Sala CRUD" }.first().id.value
        }

        val getResponse = client.get(PlaceTypeByUUID(uuid = uuid))
        assertEquals(HttpStatusCode.OK, getResponse.status)

        val editResponse = client.patch(PlaceTypeByUUID(uuid = uuid)) {
            contentType(ContentType.Application.Json)
            setBody(PlaceTypeEditDTO(name = "Sala CRUD Editada"))
        }
        assertEquals(HttpStatusCode.OK, editResponse.status)
        val editedName = withTransaction { PlaceTypeEntity.findById(uuid)!!.name }
        assertEquals("Sala CRUD Editada", editedName)

        val deleteResponse = client.delete(PlaceTypeByUUID(uuid = uuid))
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)
        val isEnabled = withTransaction { PlaceTypeEntity.findById(uuid)!!.isEnabled }
        assertTrue { !isEnabled }
    }
}
*/