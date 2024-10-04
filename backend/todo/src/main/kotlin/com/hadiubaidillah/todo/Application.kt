package com.hadiubaidillah.todo

import com.hadiubaidillah.shared.plugins.configureDatabases
import com.hadiubaidillah.shared.plugins.configureSecurity
import com.hadiubaidillah.shared.plugins.configureSerialization
import com.hadiubaidillah.shared.plugins.configureStatusPages
import com.hadiubaidillah.todo.plugins.configureKoin
import com.hadiubaidillah.todo.plugins.configureRabbitMQProducer
import com.hadiubaidillah.todo.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*


fun main() {
    embeddedServer(
        CIO,
        port = System.getenv("SERVER_PORT").toInt(),
        host = "0.0.0.0",
        watchPaths = listOf("classes", "resources"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureStatusPages()
    configureRouting()
    configureRabbitMQProducer()
}
