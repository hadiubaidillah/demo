package com.hadiubaidillah.url

import com.hadiubaidillah.shared.plugins.configureSecurity
import com.hadiubaidillah.url.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*

fun main() {
    //System.getenv().forEach { (k, v) -> println("$k => $v") }
    //println("developmentMode: $developmentMode")
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
