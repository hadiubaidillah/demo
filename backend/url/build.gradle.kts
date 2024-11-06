

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

@Suppress("UNCHECKED_CAST")
val loadEnvVariablesFunction = parent!!.extra.get("loadEnvVariablesFunction") as (String) -> Map<String, String>
val envVariables = loadEnvVariablesFunction(".env/.env") + loadEnvVariablesFunction(".env/.env.url")

group = "com.hadiubaidillah.url"
version = "1.0-SNAPSHOT"
application {
    mainClass.set("com.hadiubaidillah.url.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

tasks.named<JavaExec>("run") {
    environment(envVariables)
    //envVariables.forEach { (key, value) -> println("$key = $value") }
    //println("ini di run")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":shared"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.thymeleaf)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.43.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.43.0") // for OffsetDateTime support

    implementation(libs.logback.classic)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}



tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
repositories {
    mavenCentral()
}