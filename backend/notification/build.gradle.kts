

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

@Suppress("UNCHECKED_CAST")
val loadEnvVariablesFunction = parent!!.extra.get("loadEnvVariablesFunction") as (String) -> Map<String, String>
val envVariables = loadEnvVariablesFunction(".env/.env") + loadEnvVariablesFunction(".env/.env.notification")

group = "com.hadiubaidillah.notification"
version = "1.0-SNAPSHOT"
application {
    mainClass.set("com.hadiubaidillah.notification.ApplicationKt")
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

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.json)
    implementation(libs.mysql.connector.java)
    implementation(libs.postgresql)

    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    implementation(libs.amqp.client)
    implementation(libs.jakarta.mail)

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