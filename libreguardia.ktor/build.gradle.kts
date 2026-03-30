plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.postgresql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.exposed.dao)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.resources)
    implementation("org.flywaydb:flyway-database-postgresql:12.2.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.1.1")
    implementation("org.jetbrains.exposed:exposed-migration-core:1.1.1")
    implementation("org.jetbrains.exposed:exposed-migration-jdbc:1.1.1")
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("com.h2database:h2:2.3.232")
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.client.content.negotiation)
}
