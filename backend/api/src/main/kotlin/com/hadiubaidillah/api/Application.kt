package com.hadiubaidillah.api

import com.hadiubaidillah.api.plugins.configureRouting
import com.hadiubaidillah.api.plugins.configureTemplating
import com.hadiubaidillah.shared.plugins.configureSecurity
import com.hadiubaidillah.shared.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*

fun main() {
    embeddedServer(CIO,
        port = System.getenv("SERVER_PORT").toInt(),
        host = "0.0.0.0",
        watchPaths = listOf("classes", "resources"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureTemplating()
    configureStatusPages()
    configureRouting()
}
