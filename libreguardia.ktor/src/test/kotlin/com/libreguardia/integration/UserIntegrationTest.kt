package com.libreguardia.integration

/*
class UserIntegrationTest {
    @Test
    fun createUserAndList() = testApplication {
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
        val createResponse = client.post(UserAPI()) {
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
        val users: List<UserResponseDTO> = client.get(UserAPI()).body()
        assertTrue { users.isNotEmpty() }
        users.forEach { println(it) }
        val userUUID =
            withTransaction {
                UserTable.select(UserTable.id)
                    .where {
                        UserTable.name eq "Juan"
                    }.limit(1).map { it[UserTable.id].value }.first()
            }
        val getUser = client.get(UserAPI.UUID(uuid = userUUID))
        val userDTO = getUser.body<UserResponseDTO>()
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = getUser.status
        )
        assertTrue { userDTO.id == userUUID }
    }

    @Test
    fun hardDeleteUserWithFutureReferences() = testApplication {
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
        val createResponse = client.post(UserAPI()) {
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
        var userAPI: List<UserResponseDTO> = client.get(UserAPI()).body()
        assertTrue { userAPI.isNotEmpty() }
        userAPI.forEach { println(it) }
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
        val deleteResponse = client.delete(UserAPI.UUID.Delete(UserAPI.UUID(uuid = userUUID)))
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = deleteResponse.status
        )
        userAPI = client.get(UserAPI()).body()
        userAPI.forEach { println(it) }
        assertTrue { userAPI.isEmpty() }
        absences = withTransaction { AbsenceEntity.all() }
        assertTrue { withTransaction { absences.count().toInt() } == 2 }
        services = withTransaction { ServiceEntity.all() }
        assertTrue { withTransaction { services.count().toInt() } == 2 }
        withTransaction { services.forEach { assertTrue { it.assignedUser == null } } }
        schedules = withTransaction { ScheduleEntity.all() }
        assertTrue { withTransaction { schedules.count().toInt() } == 0 }
    }

    @Test
    fun softDeleteUserWithPastReferences() = testApplication {
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
        val createResponse = client.post(UserAPI()) {
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
        var userAPI: List<UserResponseDTO> = client.get(UserAPI()).body()
        assertTrue { userAPI.isNotEmpty() }
        userAPI.forEach { println(it) }
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
        val dateTimeNow: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val timeZone = TimeZone.currentSystemDefault()
        val dateYesterday = dateTimeNow.toInstant(timeZone).minus(1.days).toLocalDateTime(timeZone).date
        val dateTomorrow = dateTimeNow.toInstant(timeZone).plus(1.days).toLocalDateTime(timeZone).date
        val startTimeToday = dateTimeNow.toInstant(timeZone).minus(60.minutes).toLocalDateTime(timeZone).time
        val endTimeToday = dateTimeNow.toInstant(timeZone).minus(30.minutes).toLocalDateTime(timeZone).time
        withTransaction {
            AbsenceEntity.new {
                date = dateYesterday
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
                date = dateYesterday
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            val absence2 = AbsenceEntity.new {
                date = dateYesterday
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            val absence3 = AbsenceEntity.new {
                date = dateTomorrow
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            val absence4 = AbsenceEntity.new {
                date = dateTomorrow
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            val absence5 = AbsenceEntity.new {
                date = dateTomorrow
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = userEntity
            }
            val absence6 = AbsenceEntity.new {
                date = dateTomorrow
                startTime = startTimeToday
                endTime = endTimeToday
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = userEntity
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
            ServiceEntity.new {
                pointsObtained = BigDecimal.ONE
                absence = absence3
                coverUser = null
                assignedUser = userEntity
            }
            ServiceEntity.new {
                pointsObtained = BigDecimal.ONE
                absence = absence4
                coverUser = null
                assignedUser = userEntity
            }
            ServiceEntity.new {
                pointsObtained = BigDecimal.ONE
                absence = absence5
                coverUser = null
                assignedUser = null
            }
            ServiceEntity.new {
                pointsObtained = BigDecimal.ONE
                absence = absence6
                coverUser = null
                assignedUser = null
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
        assertTrue { withTransaction { absences.count().toInt() } == 8 }
        var services: SizedIterable<ServiceEntity> = withTransaction { ServiceEntity.all() }
        assertTrue { withTransaction { services.count().toInt() } == 6 }
        val deleteResponse = client.delete(UserAPI.UUID.Delete(UserAPI.UUID(uuid = userUUID)))
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = deleteResponse.status
        )
        userAPI = client.get(UserAPI()).body()
        userAPI.forEach { println(it) }
        assertTrue { userAPI.isNotEmpty() }
        assertTrue { withTransaction { !userAPI.first().isEnabled } }
        assertTrue { withTransaction { userAPI.first().isDeleted } }
        absences = withTransaction { AbsenceEntity.all() }
        assertTrue { withTransaction { absences.count().toInt() } == 6 }
        services = withTransaction { ServiceEntity.all() }
        assertTrue { withTransaction { services.count().toInt() } == 4 }
        var servicesAssigned = 0
        var servicesNotAssigned = 0
        withTransaction { services.forEach { if (it.assignedUser != null) servicesAssigned++ else servicesNotAssigned++ } }
        assertTrue { servicesAssigned == 2 }
        assertTrue { servicesNotAssigned == 2 }
        schedules = withTransaction { ScheduleEntity.all() }
        assertTrue { withTransaction { schedules.count().toInt() } == 0 }
    }

    @Test
    fun toggleEnabledUser() = testApplication {
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
        val createResponse = client.post(UserAPI()) {
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
        var userAPI: List<UserResponseDTO> = client.get(UserAPI()).body()
        assertTrue { userAPI.isNotEmpty() }
        userAPI.forEach { println(it) }
        val userUUID =
            withTransaction {
                UserTable.select(UserTable.id)
                    .where {
                        UserTable.name eq "Juan"
                    }.limit(1).map { it[UserTable.id].value }.first()
            }
        var userEntity = withTransaction { UserEntity.findById(userUUID)!! }
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
        val dateTimeNow: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val timeZone = TimeZone.currentSystemDefault()
        val dateYesterday = dateTimeNow.toInstant(timeZone).minus(1.days).toLocalDateTime(timeZone).date
        val dateTomorrow = dateTimeNow.toInstant(timeZone).plus(1.days).toLocalDateTime(timeZone).date
        val startTimeToday = dateTimeNow.toInstant(timeZone).plus(30.minutes).toLocalDateTime(timeZone).time
        val endTimeToday = dateTimeNow.toInstant(timeZone).plus(60.minutes).toLocalDateTime(timeZone).time
        withTransaction {
            val absence1 = AbsenceEntity.new {
                date = dateTimeNow.date
                startTime = startTimeToday
                endTime = endTimeToday
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            val absence2 = AbsenceEntity.new {
                date = dateTomorrow
                startTime = dateTimeNow.time
                endTime = dateTimeNow.time
                group = null
                scheduleActivity = scheduleActivityEntity
                place = placeEntity
                user = null
            }
            val absence3 = AbsenceEntity.new {
                date = dateYesterday
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
            ServiceEntity.new {
                pointsObtained = BigDecimal.ONE
                absence = absence3
                coverUser = null
                assignedUser = userEntity
            }
        }
        assertTrue { userEntity.isEnabled }
        var absences: SizedIterable<AbsenceEntity> = withTransaction { AbsenceEntity.all() }
        assertTrue { withTransaction { absences.count().toInt() } == 3 }
        var services: SizedIterable<ServiceEntity> = withTransaction { ServiceEntity.all() }
        assertTrue { withTransaction { services.count().toInt() } == 3 }
        var toggleEnabledResponse = client.patch(UserAPI.UUID.ToggleEnabled(UserAPI.UUID(uuid = userUUID))) {
            contentType(ContentType.Application.Json)
            setBody(false)
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = toggleEnabledResponse.status
        )
        userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        assertTrue { !userEntity.isEnabled }
        absences = withTransaction { AbsenceEntity.all() }
        assertTrue { withTransaction { absences.count().toInt() } == 3 }
        services = withTransaction { ServiceEntity.all() }
        assertTrue { withTransaction { services.count().toInt() } == 3 }
        var assignedServices = 0
        var notAssignedServices = 0
        withTransaction { services.forEach { if (it.assignedUser?.id?.value == userUUID) assignedServices++ else notAssignedServices++ } }
        println(assignedServices)
        println(notAssignedServices)
        assertTrue { assignedServices == 1 }
        assertTrue { notAssignedServices == 2 }
        toggleEnabledResponse = client.patch(UserAPI.UUID.ToggleEnabled(UserAPI.UUID(uuid = userUUID))) {
            contentType(ContentType.Application.Json)
            setBody(true)
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = toggleEnabledResponse.status
        )
        userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        assertTrue { userEntity.isEnabled }
    }

    @Test
    fun editUser() = testApplication {
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
            UserRoleEntity.new {
                name = "USER"
            }
        }
        val adminRoleUUID =
            withTransaction {
                UserRoleTable.select(UserRoleTable.id)
                    .where {
                        UserRoleTable.name eq "ADMIN"
                    }.limit(1).map { it[UserRoleTable.id].value }.first()
            }
        val userRoleUUID =
            withTransaction {
                UserRoleTable.select(UserRoleTable.id)
                    .where {
                        UserRoleTable.name eq "USER"
                    }.limit(1).map { it[UserRoleTable.id].value }.first()
            }
        val newName = "Mitchell"
        val newSurname = "Admin"
        val newEmail = "mitcheladmin@edu.gva.es"
        val newPhoneNumber = "123456789"
        val newPassword = "supersecretpassword"
        val newIsEnabled = false
        val newUserRoleUUID = userRoleUUID
        val createResponse = client.post(UserAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                UserCreateDTO(
                    name = "Juan",
                    surname = "Martínez Hernández",
                    email = "juanmaher@edu.gva.es",
                    phoneNumber = "000000000",
                    password = "12345678",
                    isEnabled = true,
                    userRoleUUID = adminRoleUUID
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )
        var userAPI: List<UserResponseDTO> = client.get(UserAPI()).body()
        assertTrue { userAPI.isNotEmpty() }
        userAPI.forEach { println(it) }
        val userUUID =
            withTransaction {
                UserTable.select(UserTable.id)
                    .where {
                        UserTable.name eq "Juan"
                    }.limit(1).map { it[UserTable.id].value }.first()
            }
        var userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        val oldPassword = userEntity.password
        assertTrue { userEntity.isEnabled }
        var editResponse = client.patch(UserAPI.UUID.Edit(UserAPI.UUID(uuid = userUUID))) {
            contentType(ContentType.Application.Json)
            setBody(
                UserEditDTO(
                    name = newName,
                    surname = newSurname,
                    email = newEmail,
                    phoneNumber = newPhoneNumber
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = editResponse.status
        )
        userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        assertTrue {
            userEntity.name == newName &&
                    userEntity.surname == newSurname &&
                    userEntity.email == newEmail &&
                    userEntity.phoneNumber == newPhoneNumber
        }
        editResponse = client.patch(UserAPI.UUID.Edit(UserAPI.UUID(uuid = userUUID))) {
            contentType(ContentType.Application.Json)
            setBody(
                UserEditDTO(
                    password = newPassword,
                    isEnabled = newIsEnabled,
                    userRoleUUID = newUserRoleUUID
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = editResponse.status
        )
        userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        assertTrue {
            userEntity.password != oldPassword &&
                    userEntity.isEnabled == newIsEnabled &&
                    userEntity.phoneNumber == newPhoneNumber
        }
    }

    @Test
    fun editUserProfile() = testApplication {
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
            UserRoleEntity.new {
                name = "USER"
            }
        }
        val adminRoleUUID =
            withTransaction {
                UserRoleTable.select(UserRoleTable.id)
                    .where {
                        UserRoleTable.name eq "ADMIN"
                    }.limit(1).map { it[UserRoleTable.id].value }.first()
            }
        var newPhoneNumber = "123456789"
        val currentPassword = "12345678"
        val newPassword = "supersecretpassword"
        val createResponse = client.post(UserAPI()) {
            contentType(ContentType.Application.Json)
            setBody(
                UserCreateDTO(
                    name = "Juan",
                    surname = "Martínez Hernández",
                    email = "juanmaher@edu.gva.es",
                    phoneNumber = "000000000",
                    password = currentPassword,
                    isEnabled = true,
                    userRoleUUID = adminRoleUUID
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.Created,
            actual = createResponse.status
        )
        var userAPI: List<UserResponseDTO> = client.get(UserAPI()).body()
        assertTrue { userAPI.isNotEmpty() }
        userAPI.forEach { println(it) }
        val userUUID =
            withTransaction {
                UserTable.select(UserTable.id)
                    .where {
                        UserTable.name eq "Juan"
                    }.limit(1).map { it[UserTable.id].value }.first()
            }
        var userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        val oldPassword = userEntity.password
        assertTrue { userEntity.isEnabled }
        var editResponse = client.patch(UserAPI.UUID.EditProfile(UserAPI.UUID(uuid = userUUID))) {
            contentType(ContentType.Application.Json)
            setBody(
                UserEditProfileDTO(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    phoneNumber = newPhoneNumber
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = editResponse.status
        )
        userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        assertTrue {
            userEntity.phoneNumber == newPhoneNumber &&
                    userEntity.password != oldPassword
        }
        newPhoneNumber = "99999999"
        editResponse = client.patch(UserAPI.UUID.EditProfile(UserAPI.UUID(uuid = userUUID))) {
            contentType(ContentType.Application.Json)
            setBody(
                UserEditProfileDTO(
                    phoneNumber = newPhoneNumber
                )
            )
        }
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = editResponse.status
        )
        userEntity = withTransaction { UserEntity.findById(userUUID)!! }
        assertTrue { userEntity.phoneNumber == newPhoneNumber }
    }
}
*/