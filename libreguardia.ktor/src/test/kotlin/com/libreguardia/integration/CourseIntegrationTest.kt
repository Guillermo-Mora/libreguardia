package com.libreguardia.integration

import com.libreguardia.Testing
import com.libreguardia.config.*
import com.libreguardia.db.*
import com.libreguardia.dto.CourseRequestDTO
import com.libreguardia.dto.CourseResponseDTO
import com.libreguardia.repository.CourseRepository
import com.libreguardia.routing.CoursesAPI
import com.libreguardia.service.CourseService
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CourseIntegrationTest {
    @Test
    fun createCourseAndList() = testApplication {
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
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        transaction {
            val pf = ProfessionalFamilyEntity.new {
                name = "Informática"
                isEnabled = true
            }
            pf.id
        }

        val pfUUID = transaction {
            ProfessionalFamilyTable.select(ProfessionalFamilyTable.id)
                .where { ProfessionalFamilyTable.name eq "Informática" }
                .limit(1)
                .map { it[ProfessionalFamilyTable.id].value }
                .first()
        }

        val createResponse = client.post(CoursesAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "DAM",
                    isEnabled = true,
                    professionalFamilyId = pfUUID.toString()
                )
            )
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        val createdCourse: CourseResponseDTO = createResponse.body()
        assertTrue(createdCourse.id.isNotEmpty())
        assertEquals("DAM", createdCourse.name)

        val listResponse = client.get(CoursesAPI())
        assertEquals(HttpStatusCode.OK, listResponse.status)
        val courses: List<CourseResponseDTO> = listResponse.body()
        assertTrue(courses.isNotEmpty())
    }

    @Test
    fun getCourseById() = testApplication {
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
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val pfUUID = transaction {
            val pf = ProfessionalFamilyEntity.new {
                name = "Informática"
                isEnabled = true
            }
            pf.id.value
        }

        val createResponse = client.post(CoursesAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "DAM",
                    isEnabled = true,
                    professionalFamilyId = pfUUID.toString()
                )
            )
        }
        val createdCourse: CourseResponseDTO = createResponse.body()

        val getResponse = client.get(CoursesAPI.Id(createdCourse.id))
        assertEquals(HttpStatusCode.OK, getResponse.status)
        val course: CourseResponseDTO = getResponse.body()
        assertEquals("DAM", course.name)
    }

    @Test
    fun updateCourse() = testApplication {
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
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val pfUUID = transaction {
            val pf = ProfessionalFamilyEntity.new {
                name = "Informática"
                isEnabled = true
            }
            pf.id.value
        }

        val createResponse = client.post(CoursesAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "DAM",
                    isEnabled = true,
                    professionalFamilyId = pfUUID.toString()
                )
            )
        }
        val createdCourse: CourseResponseDTO = createResponse.body()

        val updateResponse = client.put(CoursesAPI.Id(createdCourse.id)) {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "DAW",
                    isEnabled = false,
                    professionalFamilyId = pfUUID.toString()
                )
            )
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        val updatedCourse: CourseResponseDTO = updateResponse.body()
        assertEquals("DAW", updatedCourse.name)
        assertEquals(false, updatedCourse.isEnabled)
    }

    @Test
    fun deleteCourse() = testApplication {
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
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val pfUUID = transaction {
            val pf = ProfessionalFamilyEntity.new {
                name = "Informática"
                isEnabled = true
            }
            pf.id.value
        }

        val createResponse = client.post(CoursesAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "DAM",
                    isEnabled = true,
                    professionalFamilyId = pfUUID.toString()
                )
            )
        }
        val createdCourse: CourseResponseDTO = createResponse.body()

        val deleteResponse = client.delete(CoursesAPI.Id(createdCourse.id))
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val notFoundResponse = client.get(CoursesAPI.Id(createdCourse.id))
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)
    }

    @Test
    fun validationErrorReturnsBadRequest() = testApplication {
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
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
        }

        val response = client.post(CoursesAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "",
                    isEnabled = true,
                    professionalFamilyId = ""
                )
            )
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}