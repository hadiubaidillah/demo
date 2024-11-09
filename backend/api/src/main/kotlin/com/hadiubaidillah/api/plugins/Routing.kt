package com.hadiubaidillah.api.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.uri
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.isMultipart

fun Application.configureRouting() {
    install(AutoHeadResponse)
    //install(CallLogging)

    val client = HttpClient(CIO)

    routing {
        get("/") {
            call.respondText("Hello Ini API!")
        }
        get("/hello") {
            call.respondText("Hello Hadi!")
        }
        get("/json-test") {
            call.respond(mapOf("message" to "Hello, World!"))
        }
        // Define proxy routes for each service
        mapOf(
            "/notification" to "http://localhost:40082",
            "/user" to "http://localhost:40084",
            "/pos" to "http://localhost:40085",
            "/task" to "http://localhost:40086",
            "/url" to "http://localhost:40087"
        ).forEach { (routePath, targetUrl) ->
            route("$routePath/{...}") {
                handle {
                    call.proxyRequest(client, targetUrl, routePath)
                }
            }
        }
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
    }
}

suspend fun ApplicationCall.proxyRequest(client: HttpClient, targetUrl: String, prefix: String) {
    val path = request.uri.removePrefix(prefix)

    // Helper function to add all headers from incoming request to the outgoing client request
    fun HttpRequestBuilder.addAllHeaders() {
        request.headers.forEach { key, values ->
            values.forEach { value ->
                headers.append(key, value)
            }
        }
    }

    val response: HttpResponse = if (request.isMultipart()) {
        val parts = mutableListOf<PartData>()
        receiveMultipart().forEachPart { part -> parts.add(part) }

        client.submitFormWithBinaryData(
            url = "$targetUrl$path",
            formData = parts
        ) {
            addAllHeaders()
        }.also { parts.forEach { it.dispose() } }
    } else {
        client.get("$targetUrl$path") {
            addAllHeaders()
        }
    }

    // Forward response to client
    respondText(response.bodyAsText())
}