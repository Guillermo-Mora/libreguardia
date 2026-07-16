import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
}

group = "com.libreguardia"
version = "1.0.0"

application {
    mainClass = "com.libreguardia.ApplicationKt"
}

//Code from previous Ktor project. I don't think I need it with KMP
//tasks.withType<ProcessResources> {
//    val wasmOutput = file("../web/build/dist/wasmJs/productionExecutable")
//    if (wasmOutput.exists()) {
//        inputs.dir(wasmOutput)
//    }
//
//    from("../web/build/dist/wasmJs/productionExecutable") {
//        into("web")
//        include("**/*")
//    }
//    duplicatesStrategy = DuplicatesStrategy.WARN
//}

//I still don't know if I'll need this when generating the server .jar for the Ktor Server.
tasks.shadowJar {
    mergeServiceFiles{
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(projects.core)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)
    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.server.cors.jvm)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.postgresql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.exposed.dao)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.resources)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.migration.core)
    implementation(libs.exposed.migration.jdbc)
    implementation(libs.bcrypt)
    implementation(libs.ktor.client.resources)
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.ktor.client.content.negotiation)
}