package com.hadiubaidillah.pos

import com.hadiubaidillah.shared.plugins.configureSecurity
import com.hadiubaidillah.pos.plugins.configureRouting
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
    configureRouting()
}
