import java.nio.file.Files
import java.nio.file.Paths

plugins {
    kotlin("jvm") version "2.0.20"
}

group = "com.hadiubaidillah"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

// Function to load environment variables from a file and return them as a list of key-value pairs.
extra.set("loadEnvVariablesFunction", { filePath: String ->
    Files.readAllLines(Paths.get(filePath))
        .asSequence()
        .filter { it.isNotBlank() && !it.startsWith("#") }
        .map { it.split("=", limit = 2).let { (key, value) -> key.trim() to value.trim() } }
        .toMap()
})