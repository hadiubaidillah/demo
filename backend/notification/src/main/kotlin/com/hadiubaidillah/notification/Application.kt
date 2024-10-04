package com.hadiubaidillah.notification

import com.hadiubaidillah.notification.plugins.configureDatabaseInitialize
import com.hadiubaidillah.notification.plugins.configureKoin
import com.hadiubaidillah.shared.plugins.configureSecurity
import com.hadiubaidillah.notification.plugins.configureRouting
import com.hadiubaidillah.notification.plugins.configureTemplating
import com.hadiubaidillah.notification.plugins.launchRabbitMQConsumer
import com.hadiubaidillah.shared.plugins.configureDatabases
import com.hadiubaidillah.shared.plugins.configureSerialization
import com.hadiubaidillah.shared.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.CIO

fun main() {
    embeddedServer(CIO,
        port = System.getenv("SERVER_PORT").toInt(),
        host = "0.0.0.0",
        watchPaths = listOf("classes", "resources"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {

    configureKoin()
    configureDatabases()
    configureDatabaseInitialize()
    configureSerialization()
    configureSecurity()
    configureTemplating()
    configureStatusPages()
    configureRouting()
    launchRabbitMQConsumer()
}
