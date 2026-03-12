package com.libreguardia.api

import com.libreguardia.api.entity.Building
import com.libreguardia.api.entity.Place
import com.libreguardia.api.entity.PlaceType
import com.libreguardia.api.entity.Zone
import com.libreguardia.api.repository.BuildingRepository
import com.libreguardia.api.repository.PlaceRepository
import com.libreguardia.api.repository.PlaceTypeRepository
import com.libreguardia.api.repository.ZoneRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BuildingPlaceIntegrationTest {

    @Autowired
    private lateinit var buildingRepository: BuildingRepository

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var zoneRepository: ZoneRepository

    @BeforeEach
    fun cleanup() {
        placeRepository.deleteAll()
        zoneRepository.deleteAll()
        placeTypeRepository.deleteAll()
        buildingRepository.deleteAll()
    }

    @Autowired
    private lateinit var placeTypeRepository: PlaceTypeRepository

    @Autowired
    lateinit var testEntityManager: TestEntityManager

    private val log = KotlinLogging.logger { }

    @Test
    @Transactional
    fun `should save two places referencing the same building`() {

        // Crear dependencias necesarias
        val zone = zoneRepository.save(Zone(zoneName = "Zona A", places = emptySet()))
        val placeType = placeTypeRepository.save(PlaceType(placeTypeName = "Aula", places = emptySet()))
        val building = buildingRepository.save(Building(buildingName = "Edificio Principal", places = emptySet()))

        // Crear dos places con el mismo building
        val place1 = Place(
            placeName = "Aula 101",
            placeFloor = "1",
            building = building,
            zone = zone,
            placeType = placeType
        )
        val place2 = Place(
            placeName = "Aula 102",
            placeFloor = "1",
            building = building,
            zone = zone,
            placeType = placeType
        )

        placeRepository.save(place1)
        placeRepository.save(place2)
        testEntityManager.flush()
        testEntityManager.detach(building)

        log.info { "Building id: ${building.buildingId}" }

        // Recuperar el building y verificar que tiene 2 places
        val retrieved = buildingRepository.findById(building.buildingId!!).orElseThrow()

        log.info { "Retrieved building with id: ${retrieved.buildingId}" }
        log.info { "Retrieved building: $retrieved" }
    }
}