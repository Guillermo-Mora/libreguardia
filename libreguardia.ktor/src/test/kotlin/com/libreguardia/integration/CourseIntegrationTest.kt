package com.libreguardia.integration

import com.libreguardia.Testing
import com.libreguardia.config.*
import com.libreguardia.db.*
import com.libreguardia.dto.CourseRequestDTO
import com.libreguardia.dto.CourseResponseDTO
import com.libreguardia.repository.CourseRepository
import com.libreguardia.service.CourseService
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
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
                userService = null,
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        transaction {
            ProfessionalFamilyEntity.new {
                name = "Informática"
                isEnabled = true
            }
        }

        val pfUUID = transaction {
            ProfessionalFamilyTable.select(ProfessionalFamilyTable.id)
                .where { ProfessionalFamilyTable.name eq "Informática" }
                .limit(1)
                .map { it[ProfessionalFamilyTable.id].value }
                .first()
        }

        val createResponse = client.post("/api/courses") {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "DAM",
                    isEnabled = true,
                    professionalFamilyId = pfUUID.toString()
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )
        val courses: List<CourseResponseDTO> = client.get("/api/courses").body()
        assertTrue { courses.isNotEmpty() }

        val courseUUID =
            transaction {
                CourseTable.select(CourseTable.id)
                    .where {
                        CourseTable.name eq "DAM"
                    }.limit(1).map { it[CourseTable.id].value }.first()
            }
        val getCourse = client.get("/api/courses/$courseUUID")
        val courseDTO = getCourse.body<CourseResponseDTO>()
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = getCourse.status
        )
        assertTrue { courseDTO.id == courseUUID.toString() }
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
                userService = null,
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val pfUUID = transaction {
            val pf = ProfessionalFamilyEntity.new {
                name = "Informática"
                isEnabled = true
            }
            pf.id.value
        }

        val createResponse = client.post("/api/courses") {
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

        val updateResponse = client.put("/api/courses/${createdCourse.id}/edit") {
            contentType(ContentType.Application.Json)
            setBody(
                CourseRequestDTO(
                    name = "DAW",
                    isEnabled = false,
                    professionalFamilyId = pfUUID.toString()
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = updateResponse.status
        )
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
                userService = null,
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val pfUUID = transaction {
            val pf = ProfessionalFamilyEntity.new {
                name = "Informática"
                isEnabled = true
            }
            pf.id.value
        }

        val createResponse = client.post("/api/courses") {
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

        val deleteResponse = client.delete("/api/courses/${createdCourse.id}/delete")
        assertEquals(
            expected = HttpStatusCode.NoContent,
            actual = deleteResponse.status
        )
    }

    @Test
    fun courseNotFound() = testApplication {
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
                userService = null,
                courseService = CourseService(CourseRepository())
            )
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/api/courses/00000000-0000-0000-0000-000000000000")
        assertEquals(
            expected = HttpStatusCode.NotFound,
            actual = response.status
        )
    }
}