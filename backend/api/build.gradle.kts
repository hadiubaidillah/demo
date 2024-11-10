

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

@Suppress("UNCHECKED_CAST")
val loadEnvVariablesFunction = parent!!.extra.get("loadEnvVariablesFunction") as (String) -> Map<String, String>
val envVariables = loadEnvVariablesFunction(".env/.env") + loadEnvVariablesFunction(".env/.env.api")

group = "com.hadiubaidillah.api"
version = "1.0-SNAPSHOT"
application {
    mainClass.set("com.hadiubaidillah.api.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

tasks.named<JavaExec>("run") {
    environment(envVariables)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":shared"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.auto.head.response)
    implementation(libs.ktor.server.swagger)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)

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